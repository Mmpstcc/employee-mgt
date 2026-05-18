package com.example.employee.repository;

import com.example.employee.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    // Find by department
    List<Employee> findByDepartment(String department);

    // Find by status
    List<Employee> findByStatus(String status);

    // Find by email (unique)
    Optional<Employee> findByEmail(String email);

    // Search by name (case-insensitive)
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
}
