package com.nhnacademy.gateway.dto.account;

import com.nhnacademy.gateway.validation.NotBlankGroup;
import com.nhnacademy.gateway.validation.PatternGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class MemberModifyRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.", groups = NotBlankGroup.class)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
            message = "8자 이상 20자 이하, 소문자/특수문자 포함",
            groups = PatternGroup.class)
    private String password;

    @NotBlank(message = "이름을 입력해주세요.", groups = NotBlankGroup.class)
    @Length(max = 20, message = "최대 20자", groups = PatternGroup.class)
    private String name;

}
