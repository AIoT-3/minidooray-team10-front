package com.nhnacademy.gateway.dto.comment;

import com.nhnacademy.gateway.validation.NotBlankGroup;
import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentAddTaskRequest {
    @NotBlank(message = "내용을 작성해주세요", groups = NotBlankGroup.class)
    @Size(max =  500, message = "내용은 500자 이내로 작성해야 합니다.", groups = PatternGroup.class)
    String content;
}
