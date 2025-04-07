package com.Mbakara.Banking_System.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${glory-jwt.security}")
    private String SECRETE_KEY;

    @Value("${glory-jwt.expiration}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date(); // This generates the current date at that particular moment.
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username) // Encrypted the username
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(Key()) // Create a method for the signing key
                .compact();
    }

    private Key Key(){
        byte[] bytes = Decoders.BASE64.decode(SECRETE_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        System.out.println("Token Claims: " + claims);
        return claims.getSubject();
    }

    public boolean validateToken(String token){

        // JWT handling null or empty tokens
        if (token == null || token.trim().isEmpty()){
            throw new IllegalArgumentException("JWT token cannot be null or empty.");
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Key())
                    .build()
                    .parse(token);
            return true;


        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException e) {
            throw new RuntimeException(e);
        }
    }
}
