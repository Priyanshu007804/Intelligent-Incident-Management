package com.priyanshu.iims.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String email) {

        SecretKey key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
    
    public String extractUsername(String token) {
    	SecretKey key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    	return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
    			.getPayload().getSubject();
    	}
    
    public boolean isTokenValid(String token) {
    	try {
    		extractUsername(token);
    		return true;
    	}catch(Exception exception) {
    		return false;
    	}
    }
}