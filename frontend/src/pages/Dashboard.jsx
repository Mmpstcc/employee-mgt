import { useEffect, useState } from 'react';
import { getStats, getEmployees } from '../services/employeeService';

export default function Dashboard() {
  const [stats, setStats]       = useState({ total: 0, active: 0, inactive: 0 });
  const [recent, setRecent]     = useState([]);
  const [loading, setLoading]   = useState(true);

  useEffect(() => {
    Promise.all([getStats(), getEmployees()])
      .then(([s, e]) => {
        setStats(s.data);
        setRecent(e.data.slice(-5).reverse());
      })
      .finally(() => setLoading(false));
  }, []);

  const initials = (emp) =>
    (emp.firstName[0] + emp.lastName[0]).toUpperCase();

  const colors = ['#4f46e5','#0891b2','#16a34a','#d97706','#dc2626'];

  if (loading) return <div className="loading">Loading dashboard…</div>;

  return (
    <div>
      <div className="page-header">
        <div>
          <div className="page-title">Dashboard</div>
          <div className="page-subtitle">Welcome to Employee Management System</div>
        </div>
      </div>

      {/* Stats */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon blue">👥</div>
          <div>
            <div className="stat-label">Total Employees</div>
            <div className="stat-value">{stats.total}</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon green">✅</div>
          <div>
            <div className="stat-label">Active</div>
            <div className="stat-value">{stats.active}</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon red">🔴</div>
          <div>
            <div className="stat-label">Inactive</div>
            <div className="stat-value">{stats.inactive}</div>
          </div>
        </div>
      </div>

      {/* Recent employees */}
      <div className="card">
        <div className="card-header">
          <span style={{ fontWeight: 600 }}>Recent Employees</span>
        </div>
        {recent.length === 0 ? (
          <div className="empty-state">
            <div style={{ fontSize: 40 }}>👤</div>
            <p>No employees yet. Add your first employee!</p>
          </div>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Employee</th>
                <th>Department</th>
                <th>Designation</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {recent.map((emp, i) => (
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
                  <td>
                    <span className={`badge badge-${emp.status.toLowerCase()}`}>
                      {emp.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
