package com.nhnacademy.gateway.auth;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.dto.auth.AccountResponse;
import com.nhnacademy.gateway.dto.auth.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 스프링이 로그인 시 호출
 * /login -> form 데이터 읽고 LoadByUsername() 호출
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountApiClient accountApiClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // username = email;
        AccountResponse account = accountApiClient.getByEmail(username);

        if (account.status() == Status.TERMINATE) {
            throw new DisabledException("탈퇴 회원");
        }

        if (account.status() == Status.SLEEP) {
            throw new LockedException("휴면 회원");
        }

        return new AuthUser(account.id(), account.email(), account.password());
    }
}
