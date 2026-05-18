package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.dto.account.MemberModifyRequest;
import com.nhnacademy.gateway.dto.account.MemberResponse;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountApiClient accountApiClient;

    @GetMapping("/mypage")
    public String myPageForm(Model model) {
        MemberResponse response = accountApiClient.getMember();

        MemberModifyRequest form = new MemberModifyRequest();
        form.setName(response.name());

        model.addAttribute("email", response.email());
        model.addAttribute("memberModifyRequest", form);

        return "layout/mypage";
    }

    @PostMapping("/mypage")
    public String updateMember(@Validated(ValidationSequence.class) @ModelAttribute MemberModifyRequest request,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "layout/mypage";
        }

        accountApiClient.modifyMember(request);

        return "redirect:/mypage";
    }
}
