package com.oxysystem.general.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil {
    private Key key() {
        String secret_key = "oxysystemgrabdanacoglobalsolusi-f2018a9a-564a-4fcc-9470-1d6b839fbedf-c5c73c71-9b82-4375-a4bb-b5a750100ea9";
        return Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username, String tenantId) {
        return Jwts.builder()
                .claim("tenantId", tenantId)
                .setSubject(username)
                .setIssuer("oxysystem-auth")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


