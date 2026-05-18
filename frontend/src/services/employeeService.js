import axios from 'axios';

const API = axios.create({
  baseURL: 'http://192.168.2.29:8085/api',
  headers: { 'Content-Type': 'application/json' },
});

// ── Employee API calls ──────────────────────────────

export const getEmployees = (params = {}) =>
  API.get('/employees', { params });

export const getEmployee = (id) =>
  API.get(`/employees/${id}`);

export const createEmployee = (data) =>
  API.post('/employees', data);

export const updateEmployee = (id, data) =>
  API.put(`/employees/${id}`, data);

export const deleteEmployee = (id) =>
  API.delete(`/employees/${id}`);

export const getStats = () =>
  API.get('/employees/stats');

export default API;
