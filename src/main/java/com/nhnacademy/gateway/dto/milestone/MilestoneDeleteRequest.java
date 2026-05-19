package com.nhnacademy.gateway.dto.milestone;

import java.util.List;

public record MilestoneDeleteRequest (
        List<Long> milestoneIds
) {}
