import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './assets/App.css';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Employees from './pages/Employees';

export default function App() {
  return (
    <BrowserRouter>
      <div className="app-layout">
        <Sidebar />
        <main className="main-content">
          <Routes>
            <Route path="/"          element={<Dashboard />} />
            <Route path="/employees" element={<Employees />} />
            <Route path="/add"       element={<Employees openAdd />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}
