package com.example.employee.repository;
import com.example.employee.model.LeaveRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface LeaveRepository extends MongoRepository<LeaveRequest, String> {
    List<LeaveRequest> findByEmployeeId(String employeeId);
    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByEmployeeIdAndStatus(String employeeId, String status);
}
