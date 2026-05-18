package com.example.employee.service;
import com.example.employee.dto.*;
import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;
import com.example.employee.security.CustomUserDetailsService;
import com.example.employee.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;
@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    public LoginResponse login(LoginRequest req){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                req.getUsername(),req.getPassword()));
        UserDetails ud=userDetailsService.loadUserByUsername(req.getUsername());
        String token=jwtUtil.generateToken(ud);
        User u=userRepo.findByUsername(req.getUsername()).get();
        return new LoginResponse(token,u.getUsername(),u.getRoles());
    }
    public User register(RegisterRequest req){
        if(userRepo.existsByUsername(req.getUsername()))
            throw new RuntimeException("Username already exists");
        User u=new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());
        u.setRoles(req.getRoles()!=null?req.getRoles():Set.of("ROLE_EMPLOYEE"));
        return userRepo.save(u);
    }
}
