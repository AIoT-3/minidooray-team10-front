package com.nhnacademy.gateway.dto.project;

import com.nhnacademy.gateway.dto.enums.ProjectStatus;
import com.nhnacademy.gateway.validation.NotBlankGroup;
import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectModifyRequest {
    @NotBlank(message = "프로젝트명을 입력해주세요", groups = NotBlankGroup.class)
    @Size(max = 50, message = "프로젝트명은 50자 이내로 작성해야 합니다", groups = PatternGroup.class)
    String projectName;

    @NotBlank(message = "프로젝트 상태값을 선택해주세요", groups = NotBlank.class)
    ProjectStatus status;
}
