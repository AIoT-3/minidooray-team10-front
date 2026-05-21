package com.nhnacademy.gateway.dto.front;

import java.time.LocalDate;

public record TaskTimeline(
        Long id,
        String title,
        LocalDate deadline,
        int percent,
        Long milestoneId
) {}