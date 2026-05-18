import { useEffect, useState, useCallback } from 'react';
import {
  getEmployees, createEmployee,
  updateEmployee, deleteEmployee,
} from '../services/employeeService';
import EmployeeForm from '../components/EmployeeForm';
import Toast from '../components/Toast';

export default function Employees() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading]     = useState(true);
  const [search, setSearch]       = useState('');
  const [deptFilter, setDeptFilter] = useState('');
  const [showForm, setShowForm]   = useState(false);
  const [editing, setEditing]     = useState(null);
  const [toast, setToast]         = useState(null);
  const [deleting, setDeleting]   = useState(null);

  const DEPARTMENTS = ['Engineering','HR','Finance','Marketing','Operations','Sales'];
  const colors = ['#4f46e5','#0891b2','#16a34a','#d97706','#dc2626','#9333ea'];

  const initials = (emp) =>
    (emp.firstName[0] + emp.lastName[0]).toUpperCase();

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const params = {};
      if (search)     params.search     = search;
      if (deptFilter) params.department = deptFilter;
      const { data } = await getEmployees(params);
      setEmployees(data);
    } finally {
      setLoading(false);
    }
  }, [search, deptFilter]);

  useEffect(() => { load(); }, [load]);

  const handleSave = async (formData) => {
    try {
      if (editing) {
        await updateEmployee(editing.id, formData);
        setToast({ message: 'Employee updated!', type: 'success' });
      } else {
        await createEmployee(formData);
        setToast({ message: 'Employee added!', type: 'success' });
      }
      setShowForm(false);
      setEditing(null);
      load();
    } catch (err) {
      setToast({ message: err.response?.data?.error || 'Something went wrong', type: 'error' });
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this employee?')) return;
    try {
      setDeleting(id);
      await deleteEmployee(id);
      setToast({ message: 'Employee deleted', type: 'success' });
      load();
    } catch {
      setToast({ message: 'Delete failed', type: 'error' });
    } finally {
      setDeleting(null);
    }
  };

  const openEdit = (emp) => { setEditing(emp); setShowForm(true); };
  const openAdd  = ()    => { setEditing(null); setShowForm(true); };

  return (
    <div>
      {/* Header */}
      <div className="page-header">
        <div>
          <div className="page-title">Employees</div>
          <div className="page-subtitle">{employees.length} record(s) found</div>
        </div>
        <button className="btn btn-primary" onClick={openAdd}>
          ➕ Add Employee
        </button>
      </div>

      {/* Table Card */}
      <div className="card">
        <div className="card-header">
          {/* Search */}
          <div className="search-bar">
            🔍
            <input
              placeholder="Search by name…"
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>
          {/* Department filter */}
          <select
            value={deptFilter}
            onChange={e => setDeptFilter(e.target.value)}
            style={{ padding:'8px 12px', borderRadius:8, border:'1px solid #e5e7eb', fontSize:14 }}
          >
            <option value="">All Departments</option>
            {DEPARTMENTS.map(d => <option key={d}>{d}</option>)}
          </select>
        </div>

        {loading ? (
          <div className="loading">Loading employees…</div>
        ) : employees.length === 0 ? (
          <div className="empty-state">
            <div style={{ fontSize: 44 }}>🔍</div>
            <p>No employees found. Try a different search.</p>
          </div>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Employee</th>
                <th>Department</th>
                <th>Designation</th>
                <th>Salary</th>
                <th>Phone</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {employees.map((emp, i) => (
                <tr key={emp.id}>
                  <td>
                    <div className="emp-cell">
                      <div className="avatar" style={{ background: colors[i % colors.length] }}>
                        {initials(emp)}
                      </div>
                      <div>
                        <div className="emp-name">{emp.firstName} {emp.lastName}</div>
                        <div className="emp-email">{emp.email}</div>
                      </div>
                    </div>
                  </td>
                  <td>{emp.department}</td>
                  <td>{emp.designation}</td>
                  <td>₹{Number(emp.salary).toLocaleString('en-IN')}</td>
                  <td>{emp.phone || '—'}</td>
                  <td>
                    <span className={`badge badge-${emp.status.toLowerCase()}`}>
                      {emp.status}
                    </span>
                  </td>
                  <td style={{ display:'flex', gap:8 }}>
                    <button className="btn btn-edit btn-sm" onClick={() => openEdit(emp)}>
                      ✏️ Edit
                    </button>
                    <button
                      className="btn btn-danger btn-sm"
                      onClick={() => handleDelete(emp.id)}
                      disabled={deleting === emp.id}
                    >
                      🗑️ Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Form Modal */}
      {showForm && (
        <EmployeeForm
          employee={editing}
          onSave={handleSave}
          onClose={() => { setShowForm(false); setEditing(null); }}
        />
      )}

      {/* Toast */}
      {toast && (
        <Toast
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}
    </div>
  );
}
