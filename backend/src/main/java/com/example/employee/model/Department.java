package com.example.employee.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.NotBlank;
@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "departments")
public class Department {
    @Id private String id;
    @NotBlank(message = "Department name is required")
    @Indexed(unique = true)
    private String name;
    private String description;
    private String managerId;
    private String managerName;
    private int totalEmployees = 0;
    private String status = "ACTIVE";
}
