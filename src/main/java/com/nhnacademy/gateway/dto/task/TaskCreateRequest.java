package com.nhnacademy.gateway.dto.task;

import com.nhnacademy.gateway.validation.NotBlankGroup;
import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TaskCreateRequest {
    @NotBlank(message = "Task 이름을 작성해주세요", groups = NotBlankGroup.class)
    @Size(max = 50, message = "Task 이름은 50자 이내로 작성해야 합니다.", groups = PatternGroup.class)
    String title;

    @NotBlank(message = "Task 본문을 작성해주세요.", groups = NotBlankGroup.class)
    @Size(max = 500, message = "Task 본문은 500자 이내로 작성해야 합니다.", groups = PatternGroup.class)
    String content;

    @NotNull(message = "MileStone 선택은 필수입니다.", groups = NotBlankGroup.class)
    Long milestoneId;

    @NotNull(message = "Tag는 최소 1개 이상 선택해야합니다.", groups = NotBlankGroup.class)
    List<Long> tagIds;

    LocalDate startDate;
    LocalDate deadline;
}
