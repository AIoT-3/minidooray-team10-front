package com.nhnacademy.gateway.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.gateway.dto.enums.TaskStatus;
import com.nhnacademy.gateway.dto.tag.TagResponse;
import com.nhnacademy.gateway.validation.NotBlankGroup;
import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TaskModifyRequest {
    @NotBlank(message = "Task 제목을 입력해주세요.", groups = NotBlankGroup.class)
    @Size(max = 50, message = "Task 제목은 50자 이내로 작성해주세요.", groups = PatternGroup.class)
    String title;

    @NotNull(message = "Task 상태를 선택해주세요", groups = NotBlankGroup.class)
    TaskStatus status;

    @NotBlank(message = "Task 본문을 입력해주세요", groups = NotBlankGroup.class)
    @Size(max = 500, message = "Task 본문은 500자 이내로 작성해주세요", groups = PatternGroup.class)
    String content;

    @NotNull(message = "마일스톤을 선택해주세요", groups = NotBlankGroup.class)
    Long milestoneId;

    @NotEmpty(message = "태그를 최소 1개 선택해주세요.", groups = NotBlankGroup.class)
    List<Long> tagIds;

    @NotNull(message = "시작일을 선택해주세요.", groups = NotBlankGroup.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate;

    @NotNull(message = "마감일을 선택해주세요.", groups = NotBlankGroup.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate deadline;

    public TaskModifyRequest(TaskDetailResponse response) {
        this.title = response.title();
        this.status = response.status();
        this.content = response.content();
        this.milestoneId = response.milestoneId();
        this.tagIds = response.tags().stream().map(TagResponse::id).toList();
        this.startDate = response.startDate();
        this.deadline = response.deadline();
    }
}