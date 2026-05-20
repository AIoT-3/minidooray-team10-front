package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.auth.UserIdHeaderInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final UserIdHeaderInterceptor restTemplateHeaderInterceptor;

    @Bean
    public RestTemplate restTemplate() {
        // new RestTemplate() : 내부 HttpURLConnection이 PATCH 지원 안 함
        // 수정 후 : Apache HttpClient -> PATCH 지원
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        restTemplate.setInterceptors(Collections.singletonList(restTemplateHeaderInterceptor));
        return restTemplate;
    }
}