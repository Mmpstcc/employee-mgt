package com.example.employee.repository;
import com.example.employee.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.*;
public interface DepartmentRepository extends MongoRepository<Department, String> {
    Optional<Department> findByName(String name);
    List<Department> findByStatus(String status);
    boolean existsByName(String name);
}
