package com.example.employee.repository;
import com.example.employee.model.Performance;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface PerformanceRepository extends MongoRepository<Performance, String> {
    List<Performance> findByEmployeeId(String employeeId);
    List<Performance> findByYear(int year);
    List<Performance> findByStatus(String status);
}
