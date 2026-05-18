import { useState, useEffect } from 'react';

const DEPARTMENTS = ['Engineering', 'HR', 'Finance', 'Marketing', 'Operations', 'Sales'];
const DESIGNATIONS = ['Manager', 'Senior Engineer', 'Engineer', 'Analyst', 'Intern', 'Director'];

const EMPTY = {
  firstName: '', lastName: '', email: '', department: '',
  designation: '', salary: '', phone: '', address: '', status: 'ACTIVE',
};

export default function EmployeeForm({ employee, onSave, onClose }) {
  const [form, setForm] = useState(EMPTY);
  const [errors, setErrors] = useState({});
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (employee) setForm({ ...EMPTY, ...employee });
    else setForm(EMPTY);
  }, [employee]);

  const change = (e) => {
    setForm(f => ({ ...f, [e.target.name]: e.target.value }));
    setErrors(er => ({ ...er, [e.target.name]: '' }));
  };

  const validate = () => {
    const e = {};
    if (!form.firstName.trim()) e.firstName = 'Required';
    if (!form.lastName.trim())  e.lastName  = 'Required';
    if (!form.email.trim())     e.email     = 'Required';
    else if (!/\S+@\S+\.\S+/.test(form.email)) e.email = 'Invalid email';
    if (!form.department)       e.department  = 'Required';
    if (!form.designation)      e.designation = 'Required';
    if (!form.salary)           e.salary = 'Required';
    else if (isNaN(form.salary) || Number(form.salary) < 0) e.salary = 'Enter a valid salary';
    return e;
  };

  const submit = async () => {
    const errs = validate();
    if (Object.keys(errs).length) { setErrors(errs); return; }
    setSaving(true);
    try {
      await onSave({ ...form, salary: Number(form.salary) });
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={e => e.target === e.currentTarget && onClose()}>
      <div className="modal">
        <div className="modal-header">
          <span className="modal-title">{employee ? '✏️ Edit Employee' : '➕ Add Employee'}</span>
          <button className="btn btn-sm" onClick={onClose}>✕</button>
        </div>

        <div className="modal-body">
          <div className="form-grid">
            <div className="form-group">
              <label>First Name *</label>
              <input name="firstName" value={form.firstName} onChange={change} placeholder="John"/>
              {errors.firstName && <span className="error-msg">{errors.firstName}</span>}
            </div>

            <div className="form-group">
              <label>Last Name *</label>
              <input name="lastName" value={form.lastName} onChange={change} placeholder="Doe"/>
              {errors.lastName && <span className="error-msg">{errors.lastName}</span>}
            </div>

            <div className="form-group full">
              <label>Email *</label>
              <input name="email" value={form.email} onChange={change} placeholder="john@company.com"/>
              {errors.email && <span className="error-msg">{errors.email}</span>}
            </div>

            <div className="form-group">
              <label>Department *</label>
              <select name="department" value={form.department} onChange={change}>
                <option value="">Select department</option>
                {DEPARTMENTS.map(d => <option key={d}>{d}</option>)}
              </select>
              {errors.department && <span className="error-msg">{errors.department}</span>}
            </div>

            <div className="form-group">
              <label>Designation *</label>
              <select name="designation" value={form.designation} onChange={change}>
                <option value="">Select designation</option>
                {DESIGNATIONS.map(d => <option key={d}>{d}</option>)}
              </select>
              {errors.designation && <span className="error-msg">{errors.designation}</span>}
            </div>

            <div className="form-group">
              <label>Salary (₹) *</label>
              <input name="salary" type="number" value={form.salary} onChange={change} placeholder="50000"/>
              {errors.salary && <span className="error-msg">{errors.salary}</span>}
            </div>

            <div className="form-group">
              <label>Phone</label>
              <input name="phone" value={form.phone} onChange={change} placeholder="+91 9000000000"/>
            </div>

            <div className="form-group">
              <label>Status</label>
              <select name="status" value={form.status} onChange={change}>
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Inactive</option>
              </select>
            </div>

            <div className="form-group full">
              <label>Address</label>
              <textarea name="address" value={form.address} onChange={change}
                rows={2} placeholder="Street, City, State"/>
            </div>
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn" onClick={onClose}>Cancel</button>
          <button className="btn btn-primary" onClick={submit} disabled={saving}>
            {saving ? 'Saving…' : (employee ? 'Update Employee' : 'Add Employee')}
          </button>
        </div>
      </div>
    </div>
  );
}
