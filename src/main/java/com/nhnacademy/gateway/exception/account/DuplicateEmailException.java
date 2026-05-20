package com.nhnacademy.gateway.exception.account;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("이미 존재하는 Email입니다.");
    }
}
