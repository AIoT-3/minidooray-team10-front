package com.nhnacademy.gateway.controller.auth;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.dto.auth.SignUpRequest;
import com.nhnacademy.gateway.exception.account.DuplicateEmailException;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final AccountApiClient accountApiClient;

    @GetMapping
    public String signUpForm(Model model, Authentication authentication) {
        // 입력 받은 값을 dto에 넣을 수 있음
        model.addAttribute("signUpRequest", new SignUpRequest());

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/"; // 로그인 o -> home
        }

        return "auth/signup";
    }

    @PostMapping
    public String signUp(@Validated(ValidationSequence.class) @ModelAttribute SignUpRequest request,
                         BindingResult bindingResult,
                         Model model) {

        if(bindingResult.hasErrors()) {
            return "auth/signup";
        }

        try {
            accountApiClient.signUp(request);
        }catch (DuplicateEmailException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "/auth/signup";
        }

        return "redirect:/login";
    }
}
