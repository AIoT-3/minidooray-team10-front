package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.dto.account.request.MemberModifyRequest;
import com.nhnacademy.gateway.dto.account.response.MemberResponse;
import com.nhnacademy.gateway.validation.ValidationSequence;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

        MemberModifyRequest form = new MemberModifyRequest(response.name());

        model.addAttribute("email", response.email());
        model.addAttribute("memberModifyRequest", form);

        return "account/mypage";
    }

    @PostMapping("/mypage")
    public String updateMember(@Validated(ValidationSequence.class) @ModelAttribute MemberModifyRequest request,
                               BindingResult bindingResult,
                               Model model) {
        if(bindingResult.hasErrors()) {
            MemberResponse response = accountApiClient.getMember();
            model.addAttribute("email", response.email());
            return "account/mypage";
        }

        accountApiClient.modifyMember(request);

        return "redirect:/mypage";
    }

    @PostMapping("/withdraw")
    public String deleteMember(HttpServletRequest request) {

        accountApiClient.deleteMember(); // member 상태 변경

        // 세션 정리
        SecurityContextHolder.clearContext();
        request.getSession().invalidate(); // Redis에 저장된 session도 같이 삭제됨

        return "redirect:/login";
    }

    @PostMapping("/dormant/unlock")
    public String dormantUnlock(HttpSession session) {

        Long memberId = (Long) session.getAttribute("dormantMemberId");

        accountApiClient.dormantUnlock(memberId);

        session.removeAttribute("dormantMemberId");
        return "redirect:/login";
    }
}
