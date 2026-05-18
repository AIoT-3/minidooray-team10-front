package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.auth.AuthUser;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 나가는 HTTP 요청 처리
 * 세션에 저장된 로그인 사용자 ID를 꺼내서
 * 내부 API 호출 헤더에 자동으로 붙여주는 인터셉터
 */
@Component
public class UserIdHeaderInterceptor implements ClientHttpRequestInterceptor {

    private static final String USER_ID = "X-USER-ID";

    // RestTemplate이 요청 보내기 직전에 호출
    @Override
    public ClientHttpResponse intercept(HttpRequest outRequest, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof AuthUser user) {
            outRequest.getHeaders().add(USER_ID, String.valueOf(user.getId()));
        }

        return execution.execute(outRequest, body);
    }
}