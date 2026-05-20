package com.nhnacademy.gateway.exception.account;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("존재하지 않는 멤버입니다.");
    }
}
