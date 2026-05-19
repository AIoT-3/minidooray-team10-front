package com.nhnacademy.gateway.dto.auth;

public record AccountResponse(
        Long id, // AI
        String email,
        String password,
        Status status
) {
}