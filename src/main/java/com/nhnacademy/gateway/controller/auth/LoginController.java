package com.nhnacademy.gateway.controller.auth;

import com.nhnacademy.gateway.dto.auth.AccountResponse;
import com.nhnacademy.gateway.dto.auth.LoginRequest;
import com.nhnacademy.gateway.exception.LoginFailedException;
import com.nhnacademy.gateway.service.AccountService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final AccountService accountService;

    @GetMapping
    public String form() {
        return "/auth/login";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute LoginRequest request, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "/auth/login";
        }

        try {
            AccountResponse account = accountService.login(request);
            session.setAttribute("userId", account.id()); // 세션 생성

            return "redirect:/";
        } catch (LoginFailedException e) {
            bindingResult.reject(
                    "login.fail",
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );

            return "/auth/login";
        }
    }
}