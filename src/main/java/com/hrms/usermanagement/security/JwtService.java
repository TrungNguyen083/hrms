package com.hrms.usermanagement.security;

import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {
    @Value("${SECRET}")
    private String jwtSecret;
    private final int JwtExpiration = 86400;

    public String genToken(String username) {
        Map header = new HashMap<String, String>();
        header.put("alg", "256");

        Claims claims = new DefaultClaims();
        String jwt = Jwts.builder().setHeader(header).setClaims(claims).compact();

        return jwt;
    }

    public String generateToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return com.auth0.jwt.JWT.create()
                .withClaim("username", username)
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + JwtExpiration * 1000))
                .sign(algorithm);
    }

    public String getUsername(String token) {
        return com.auth0.jwt.JWT.decode(token).getClaim("username").asString();
    }

    public boolean validateToken(String token) {
        try {
            com.auth0.jwt.JWT.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    int[] a = new int[2*10^5];

    public boolean isExpired(String token) {
        try {
            com.auth0.jwt.JWT.decode(token).getExpiresAt();
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}