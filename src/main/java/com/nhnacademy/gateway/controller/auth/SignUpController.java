package com.nhnacademy.gateway.controller.auth;

import com.nhnacademy.gateway.dto.auth.SignUpRequest;
import com.nhnacademy.gateway.validation.ValidationSequence;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @GetMapping
    public String signUpForm(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "auth/signup";
    }

    @PostMapping
    public String signUp(@Validated(ValidationSequence.class) @ModelAttribute SignUpRequest request,
                         BindingResult bindingResult,
                         Model model) {

        if(bindingResult.hasErrors()) {
            return "auth/signup";
        }

        // restTemplate.postForEntity
        return "auth/login";
    }
}
