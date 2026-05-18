package com.nhnacademy.gateway.dto.auth;

import com.nhnacademy.gateway.validation.NotBlankGroup;
import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(

        @NotBlank(message = "이메일을 입력해주세요.", groups = NotBlankGroup.class)
        @Email(message = "이메일 형식이 올바르지 않습니다.", groups = PatternGroup.class)
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.", groups = NotBlankGroup.class)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
                message = "8자 이상 20자 이하, 소문자/특수문자 포함",
                groups = PatternGroup.class)
        String password
) {
}