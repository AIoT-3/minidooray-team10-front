package com.nhnacademy.gateway.dto.project;

import com.nhnacademy.gateway.dto.enums.ProjectStatus;

import java.time.ZonedDateTime;

public record ProjectResponse (
        Long id,
        String name,
        ProjectStatus status,
        ZonedDateTime createdAt
){
}
