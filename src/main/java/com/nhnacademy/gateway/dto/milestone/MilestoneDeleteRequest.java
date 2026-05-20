package com.nhnacademy.gateway.dto.milestone;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MilestoneDeleteRequest {
    @NotNull(message = "삭제할 마일스톤을 1개 이상 선택해주세요.")
    private List<Long> milestoneIds;
}
