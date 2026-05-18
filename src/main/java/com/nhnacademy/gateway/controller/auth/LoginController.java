package com.nhnacademy.gateway.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String terminate,
                        Model model,
                        Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/"; // 로그인 o -> home
        }

        if (error != null) {
            model.addAttribute("errorMessage", "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if(terminate != null) {
            model.addAttribute("errorMessage", "탈퇴 처리된 계정입니다.");
        }

        return "auth/login";
    }
}
