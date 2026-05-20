package com.nhnacademy.gateway.auth.handler;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.dto.auth.AccountResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final AccountApiClient accountApiClient;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        if (ex.getCause() instanceof LockedException) { // 휴면회원
            String email = request.getParameter("email");
            AccountResponse member = accountApiClient.getByEmail(email);

            request.getSession().setAttribute(
                    "dormantMemberId",
                    member.id()
            );

            setDefaultFailureUrl("/login?dormant");
        } else if (ex.getCause() instanceof DisabledException) { // 탈퇴회원
            setDefaultFailureUrl("/login?terminate");
        } else {
            setDefaultFailureUrl("/login?error");
        }

        super.onAuthenticationFailure(request, response, ex);
    }
}