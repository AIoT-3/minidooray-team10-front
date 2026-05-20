package com.nhnacademy.gateway.dto.task;


import com.nhnacademy.gateway.dto.enums.TaskStatus;
import com.nhnacademy.gateway.dto.tag.TagResponse;

import java.time.LocalDate;
import java.util.List;

public record TaskDetailResponse (
        Long id,
        String title,
        TaskStatus status,
        String content,
        long writerId,
        Long milestoneId,
        String milestoneName,
        LocalDate startDate,
        LocalDate deadline,
        List<TagResponse> tags
) {
}