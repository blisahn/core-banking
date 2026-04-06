package com.devblo.infrastructure.security;

import com.devblo.common.security.TokenGeneratorPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @Override
    public String generateToken(String userId, String email, String role, String customerId) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        var builder = Jwts.builder()
                .id(UUID.randomUUID().toString()) // jti (JWT ID)
                .issuer(issuer)                   // iss (Issuer)
                .audience().add(audience).and()   // aud (Audience) JJWT 0.12 syntax
                .subject(userId)                  // sub (User ID)
                .claim("email", email)            // Custom Claim
                .claim("role", role);             // Custom Claim

        if (customerId != null) {
            builder.claim("customerId", customerId);
        }

        return builder
                .issuedAt(new Date())             // iat
                .expiration(new Date(System.currentTimeMillis() + expirationMs)) // exp
                .signWith(key)
                .compact();
    }
}
