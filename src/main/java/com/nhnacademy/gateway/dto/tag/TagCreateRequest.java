package com.nhnacademy.gateway.dto.tag;


import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagCreateRequest  {
    @NotBlank(message = "태그명을 입력해주세요.", groups = NotBlank.class)
    @Size(max = 20, message = "태그명은 20자 이내로 작성해야 합니다.", groups = PatternGroup.class)
    String name;
}