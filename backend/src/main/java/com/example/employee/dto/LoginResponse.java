package com.example.employee.dto;
import lombok.*;
import java.util.Set;
@Data @AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private Set<String> roles;
}
