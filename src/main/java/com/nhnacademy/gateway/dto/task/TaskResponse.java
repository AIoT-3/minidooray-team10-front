package com.nhnacademy.gateway.dto.task;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String projectName,
        String title,
        Long milestoneId,
        String milestoneName,
        LocalDate startDate,
        LocalDate deadline
){ }