package com.example.employee.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component @RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)
            throws ServletException,IOException {
        String h=req.getHeader("Authorization");
        if(h!=null&&h.startsWith("Bearer ")){
            try{
                String t=h.substring(7);
                String u=jwtUtil.extractUsername(t);
                if(u!=null&&SecurityContextHolder.getContext().getAuthentication()==null){
                    UserDetails ud=userDetailsService.loadUserByUsername(u);
                    if(jwtUtil.validateToken(t,ud))
                        SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(ud,null,ud.getAuthorities()));
                }
            }catch(Exception ignored){}
        }
        chain.doFilter(req,res);
    }
}
