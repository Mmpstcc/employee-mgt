import { NavLink } from 'react-router-dom';

export default function Sidebar() {
  return (
    <div className="sidebar">
      <div className="sidebar-logo">
        👥 <span>EmpTrack</span>
      </div>
      <nav className="sidebar-nav">
        <NavLink
          to="/"
          end
          className={({ isActive }) => 'nav-item' + (isActive ? ' active' : '')}
        >
          <span className="nav-icon">📊</span> Dashboard
        </NavLink>
        <NavLink
          to="/employees"
          className={({ isActive }) => 'nav-item' + (isActive ? ' active' : '')}
        >
          <span className="nav-icon">👥</span> Employees
        </NavLink>
        <NavLink
          to="/add"
          className={({ isActive }) => 'nav-item' + (isActive ? ' active' : '')}
        >
          <span className="nav-icon">➕</span> Add Employee
        </NavLink>
      </nav>
    </div>
  );
}
