package com.nhnacademy.gateway.controller.auth;

import com.nhnacademy.gateway.dto.auth.SignUpRequest;
import com.nhnacademy.gateway.exception.DuplicateEmailException;
import com.nhnacademy.gateway.service.AccountService;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final AccountService accountService;

    @GetMapping
    public String signUpForm(Model model) {
        // 입력 받은 값을 dto에 넣을 수 있음
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "auth/signup";
    }

    @PostMapping
    public String signUp(@Validated(ValidationSequence.class) @ModelAttribute SignUpRequest request,
                         BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "auth/signup";
        }

        try {
            accountService.signUp(request);
        }catch (DuplicateEmailException e) {
            // email 필드에 validation 에러 추가
            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "이미 존재하는 이메일입니다."
            );
            return "auth/signup";
        }

        return "redirect:/login";
    }
}
