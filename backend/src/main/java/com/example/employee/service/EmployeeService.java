package com.example.employee.service;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    // Get all employees
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // Get one employee by ID
    public Employee getEmployeeById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    // Create new employee
    public Employee createEmployee(Employee employee) {
        // Check email uniqueness
        repository.findByEmail(employee.getEmail()).ifPresent(e -> {
            throw new RuntimeException("Email already exists: " + employee.getEmail());
        });
        return repository.save(employee);
    }

    // Update existing employee
    public Employee updateEmployee(String id, Employee updated) {
        Employee existing = getEmployeeById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setDepartment(updated.getDepartment());
        existing.setDesignation(updated.getDesignation());
        existing.setSalary(updated.getSalary());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setStatus(updated.getStatus());
        return repository.save(existing);
    }

    // Delete employee
    public void deleteEmployee(String id) {
        getEmployeeById(id); // throws if not found
        repository.deleteById(id);
    }

    // Search employees by name
    public List<Employee> searchEmployees(String query) {
        return repository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
    }

    // Get employees by department
    public List<Employee> getByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    // Dashboard stats
    public java.util.Map<String, Object> getStats() {
        long total = repository.count();
        long active = repository.findByStatus("ACTIVE").size();
        long inactive = repository.findByStatus("INACTIVE").size();

        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("total", total);
        stats.put("active", active);
        stats.put("inactive", inactive);
        return stats;
    }
}
