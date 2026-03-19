package com.devblo.common.security;

public interface TokenGeneratorPort {
    String generateToken(String customerId, String email, String role);
}
