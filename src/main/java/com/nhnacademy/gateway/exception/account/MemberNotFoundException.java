package com.nhnacademy.gateway.exception.account;

import com.nhnacademy.gateway.exception.ApiException;

public class MemberNotFoundException extends ApiException {
    public MemberNotFoundException(int status) {
        super(status, "존재하지 않는 멤버입니다.");
    }
}
