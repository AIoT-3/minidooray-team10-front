package com.nhnacademy.gateway.dto.project;

public record ProjectMemberResponse(
        long userId,
        Role role
) {}