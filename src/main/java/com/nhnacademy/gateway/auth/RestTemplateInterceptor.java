package com.nhnacademy.gateway.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.io.IOException;

/**
 * 세션에 저장된 로그인 사용자 ID를 꺼내서
 * 내부 API 호출 헤더에 자동으로 붙여주는 인터셉터
 */
@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private static final String USER_ID = "X-USER-ID";

    // RestTemplate이 요청 보내기 직전에 호출
    @Override
    public ClientHttpResponse intercept(HttpRequest outRequest, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // 현재 스레드의 inbound request context 가져옴
        // = 브라우저가 gateway에 보낸 요청
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            Long userId = (Long) request.getSession().getAttribute("userId");

            if (userId != null) {
                outRequest.getHeaders().set(USER_ID, userId.toString());
            }
        }
        return execution.execute(outRequest, body);
    }
}