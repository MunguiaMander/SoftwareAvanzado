package com.mander.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key;
    private final Duration accessTokenTtl;

    public JwtService(@Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-ttl}") Duration accessTokenTtl) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenTtl = accessTokenTtl;
    }

    public String generate(String username, String role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenTtl.toMillis()))
                .signWith(key)
                .compact();
    }

    public Claims parse(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Duration getAccessTokenTtl() {
        return accessTokenTtl;
    }
}
