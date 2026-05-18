
package com.example.employee.repository;
import com.example.employee.model.Payroll;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.*;
public interface PayrollRepository extends MongoRepository<Payroll, String> {
    List<Payroll> findByEmployeeId(String employeeId);
    Optional<Payroll> findByEmployeeIdAndMonthAndYear(String employeeId, String month, int year);
    List<Payroll> findByMonthAndYear(String month, int year);
}
