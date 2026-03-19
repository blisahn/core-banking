package com.devblo.auth.dto;

public record AuthTokenDto(String accessToken, long expiresIn) {
}
