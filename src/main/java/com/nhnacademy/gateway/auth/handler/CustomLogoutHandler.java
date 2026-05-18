package com.nhnacademy.gateway.auth.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final StringRedisTemplate redisTemplate;
    private static final String COOKIE_NAME = "SESSION";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) {
        String sessionId = null;

        if(request.getSession(false) != null) {
            sessionId = request.getSession(false).getId();
        }

        if(sessionId != null) {
            redisTemplate.delete("spring:session:" + sessionId);
        }

        Cookie deleteCookie = new Cookie(COOKIE_NAME, null);
        deleteCookie.setPath("/"); // 전체 경로 쿠키 제거
        deleteCookie.setMaxAge(0);
        response.addCookie(deleteCookie);

        SecurityContextHolder.clearContext();
    }
}
