package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.dto.auth.AccountResponse;
import com.nhnacademy.gateway.dto.auth.LoginRequest;
import com.nhnacademy.gateway.dto.auth.SignUpRequest;

public interface AccountService {
    void signUp(SignUpRequest request);
    AccountResponse login(LoginRequest request);
}
