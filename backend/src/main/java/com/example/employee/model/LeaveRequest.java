package com.example.employee.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "leave_requests")
public class LeaveRequest {
    @Id private String id;
    @NotBlank private String employeeId;
    @NotBlank private String employeeName;
    @NotBlank private String leaveType;
    @NotNull private LocalDate startDate;
    @NotNull private LocalDate endDate;
    private int totalDays;
    private String reason;
    private String status = "PENDING";
    private String approvedBy;
    private String rejectionReason;
    private LocalDate appliedDate = LocalDate.now();
}
