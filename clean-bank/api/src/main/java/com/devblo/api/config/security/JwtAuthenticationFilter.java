package com.devblo.api.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;


    @Override
    protected void doFilterInternal(HttpServletRequest request, // <-- Büyük S ile düzeltildi
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException { // <-- ServletException eklendi

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Token yoksa devam et (SecurityConfig'de patlayacak zaten)
        }

        String token = authHeader.substring(7);

        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

            // Token'ı çöz ve doğrula
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer(issuer)
                    .requireAudience(audience)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String customerId = claims.getSubject();
            String role = claims.get("role", String.class);

            // Spring Security Context'ine kimliği yerleştir
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    customerId, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Token geçersiz veya süresi dolmuş
            SecurityContextHolder.clearContext();
//             log.warn("Geçersiz token denemesi: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}