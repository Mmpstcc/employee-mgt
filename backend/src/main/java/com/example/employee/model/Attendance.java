package com.example.employee.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalTime;
@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "attendance")
public class Attendance {
    @Id private String id;
    private String employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private double hoursWorked;
    private String status = "PRESENT";
    private String remarks;
}
