package com.nhnacademy.gateway.dto.auth;

import com.nhnacademy.gateway.dto.enums.Status;

public record AccountResponse(
        Long id, // AI
        String email,
        String password,
        Status status
) {
}