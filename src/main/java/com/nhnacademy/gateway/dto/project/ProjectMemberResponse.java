package com.nhnacademy.gateway.dto.project;

import com.nhnacademy.gateway.dto.enums.Role;

public record ProjectMemberResponse(
        long userId,
        Role role
) {}