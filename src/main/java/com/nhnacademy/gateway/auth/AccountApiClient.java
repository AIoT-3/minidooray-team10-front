package com.nhnacademy.gateway.auth;

import com.nhnacademy.gateway.dto.ErrorResponse;
import com.nhnacademy.gateway.dto.auth.AccountResponse;
import com.nhnacademy.gateway.dto.auth.SignUpRequest;
import com.nhnacademy.gateway.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class AccountApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.account.url}")
    private String accountApiUrl;

    /**
     * 회원가입 요청 (email, password, name)
     */
    public void signUp(SignUpRequest request) {
        try {
            restTemplate.postForEntity(
                    // TODO-R signup -> accounts가 더 restful (리소스 중심)
                    accountApiUrl + "/signup",
                    request,
                    Void.class
            );
        }catch (HttpClientErrorException e) {
            ErrorResponse response = parse(e);

            if("A010".equals(response.code())) {
                throw new DuplicateEmailException();
            }

            throw e;
        }
    }

    public AccountResponse login(String email) {
        return restTemplate.getForObject(
                accountApiUrl + "/accounts/email/{email}",
                AccountResponse.class,
                email
        );
    }

    private ErrorResponse parse(HttpClientErrorException e) {
        try {
            return objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
        } catch (Exception ex) {
            throw e;
        }
    }
}
