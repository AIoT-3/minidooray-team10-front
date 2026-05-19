package com.nhnacademy.gateway.dto.project;

import java.time.ZonedDateTime;

public record ProjectResponse (
        Long id,
        String name,
        ProjectStatus status,
        ZonedDateTime createdAt
){
}
