package com.nhnacademy.gateway.service.impl;

import com.nhnacademy.gateway.auth.AccountApiClient;
import com.nhnacademy.gateway.dto.auth.AccountResponse;
import com.nhnacademy.gateway.dto.auth.LoginRequest;
import com.nhnacademy.gateway.dto.auth.SignUpRequest;
import com.nhnacademy.gateway.exception.LoginFailedException;
import com.nhnacademy.gateway.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

   private final AccountApiClient accountApiClient;
   private final PasswordEncoder passwordEncoder; // 비밀번호 일치 확인 (로그인)
   
    @Override
    public void signUp(SignUpRequest request) {
        accountApiClient.signUp(request);
    }

    @Override
    public AccountResponse login(LoginRequest request) {
        AccountResponse response = accountApiClient.login(request.email());

        if(request == null || !passwordEncoder.matches(request.password(), response.password())) {
            throw new LoginFailedException();
        }

        return response;
    }
}
