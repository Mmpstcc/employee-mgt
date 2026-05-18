package com.example.employee.repository;
import com.example.employee.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.*;
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByEmployeeId(String employeeId);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId, LocalDate start, LocalDate end);
    Optional<Attendance> findByEmployeeIdAndDate(String employeeId, LocalDate date);
}
