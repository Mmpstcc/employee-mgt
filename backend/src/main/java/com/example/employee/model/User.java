package com.example.employee.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.util.Set;
@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id private String id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
    private boolean active = true;
}
