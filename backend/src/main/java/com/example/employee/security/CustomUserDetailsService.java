package com.example.employee.security;
import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u=userRepo.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRoles().stream()
                    .map(r->r.replace("ROLE_",""))
                    .toArray(String[]::new))
                .build();
    }
}
