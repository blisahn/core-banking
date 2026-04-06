package com.devblo.common.security;

public interface TokenGeneratorPort {
    String generateToken(String userId, String email, String role, String customerId);
}
