package com.nhnacademy.gateway.handler;

import com.nhnacademy.gateway.dto.auth.SignUpRequest;
import com.nhnacademy.gateway.exception.DuplicateEmailException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AccountExceptionHandler {

    // 회원가입 시 이메일 중복
    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmail(DuplicateEmailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "redirect:/mypage";
    }
}
