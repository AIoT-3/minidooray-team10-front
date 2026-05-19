package com.nhnacademy.gateway.dto.milestone;

import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MilestoneCreateRequest{
    @NotBlank(message = "마일스톤명을 입력해주세요.", groups = NotBlank.class)
    @Size(max = 20, message = "마일스톤명은 20자 이내로 작성해야 합니다.", groups = PatternGroup.class)
    String name;
}