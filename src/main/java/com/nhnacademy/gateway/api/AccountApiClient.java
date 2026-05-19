package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.ErrorResponse;
import com.nhnacademy.gateway.dto.account.MemberModifyRequest;
import com.nhnacademy.gateway.dto.account.MemberNameResponse;
import com.nhnacademy.gateway.dto.account.MemberResponse;
import com.nhnacademy.gateway.dto.auth.AccountResponse;
import com.nhnacademy.gateway.dto.auth.SignUpRequest;
import com.nhnacademy.gateway.exception.ApiException;
import com.nhnacademy.gateway.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@Slf4j
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
                    accountApiUrl + "/signup",
                    request,
                    Void.class
            );

            log.debug("회원가입 요청");
        }catch (HttpClientErrorException e) {
            ErrorResponse response = parse(e);
            if("A010".equals(response.code())) {
                throw new DuplicateEmailException();
            }
            throw new ApiException(response.status(), response.message());
        }
    }

    /**
     * 로그인
     * email 전송 -> user 정보 받아오기
     */
    public AccountResponse getByEmail(String email) {
        return restTemplate.getForObject(
                accountApiUrl + "/members/by-email?email=" + email,
                AccountResponse.class
        );
    }

    /**
     * 로그인 성공 시 로그인 시간 업데이트 요청
     */
    public void loginAtUpdate() {
        restTemplate.patchForObject(
                accountApiUrl + "/members/me",
                null,
                Void.class
        );
    }

    /**
     * 회원정보조회 (email, name)
     */
    public MemberResponse getMember() {
        return restTemplate.getForObject(
                accountApiUrl + "/members/me",
                MemberResponse.class
        );
    }

    /**
     * 회원정보수정 (password, name)
     */
    public void modifyMember(MemberModifyRequest request) {
        restTemplate.put(
                accountApiUrl + "/members/me",
                request,
                Void.class
        );
    }

    /**
     * 휴면해제
     */
    public void dormantUnlock(long memberId) {
        restTemplate.put(
                accountApiUrl + "/members/" + memberId + "/active",
                null
        );
    }

    /**
     * 회원탈퇴
     */
    public void deleteMember() {
        restTemplate.delete(accountApiUrl + "/members/me/withdraw");
    }

    /**
     * 회원 이름 반환
     */
    public MemberNameResponse getMemberName() {
        return restTemplate.getForObject(
                accountApiUrl + "/members/me/name",
                MemberNameResponse.class
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
