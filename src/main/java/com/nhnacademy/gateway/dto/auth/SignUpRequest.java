package com.nhnacademy.gateway.dto.auth;

import com.nhnacademy.gateway.validation.NotBlankGroup;
import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "이메일을 입력해주세요.", groups = NotBlankGroup.class)
    @Email(message = "이메일 형식이 올바르지 않습니다.", groups = PatternGroup.class)
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.", groups = NotBlankGroup.class)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
            message = "8자 이상 20자 이하, 특수문자 포함",
            groups = PatternGroup.class)
    private String password;

    @NotBlank(message = "이름을 입력해주세요.", groups = NotBlankGroup.class)
    @Length(max = 20, message = "최대 20자", groups = PatternGroup.class)
    private String name;
}
