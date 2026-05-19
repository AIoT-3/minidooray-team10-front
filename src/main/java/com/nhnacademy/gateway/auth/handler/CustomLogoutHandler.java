package com.nhnacademy.gateway.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            // 현재 세션 폐기, Spring Session이 감지하여 Redis에서 삭제
            session.invalidate();
        }

        SecurityContextHolder.clearContext();
    }
}
