package com.example.employee.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "performance")
public class Performance {
    @Id private String id;
    private String employeeId;
    private String employeeName;
    private String reviewerId;
    private String reviewerName;
    private String reviewPeriod;
    private int year;
    private double technicalSkills;
    private double communication;
    private double teamwork;
    private double leadership;
    private double punctuality;
    private double overallRating;
    private String strengths;
    private String improvements;
    private String comments;
    private String status = "PENDING";
    private LocalDate reviewDate = LocalDate.now();
}
