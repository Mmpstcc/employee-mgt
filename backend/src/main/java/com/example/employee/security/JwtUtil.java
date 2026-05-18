package com.example.employee.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private long expiration;
    private Key getKey(){return Keys.hmacShaKeyFor(secret.getBytes());}
    public String generateToken(UserDetails u){
        return Jwts.builder().setSubject(u.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getKey(),SignatureAlgorithm.HS256).compact();
    }
    public String extractUsername(String t){
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(t).getBody().getSubject();
    }
    public boolean validateToken(String t,UserDetails u){
        try{
            String user=extractUsername(t);
            Date exp=Jwts.parserBuilder().setSigningKey(getKey()).build()
                    .parseClaimsJws(t).getBody().getExpiration();
            return user.equals(u.getUsername())&&exp.after(new Date());
        }catch(Exception e){return false;}
    }
}
