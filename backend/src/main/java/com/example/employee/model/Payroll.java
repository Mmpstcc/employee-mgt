package com.example.employee.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "payroll")
public class Payroll {
    @Id private String id;
    private String employeeId;
    private String employeeName;
    private String department;
    private String month;
    private int year;
    private double basicSalary;
    private double hra;
    private double ta;
    private double da;
    private double grossSalary;
    private double pf;
    private double tax;
    private double totalDeductions;
    private double netSalary;
    private LocalDate generatedDate = LocalDate.now();
    private String status = "GENERATED";
}
