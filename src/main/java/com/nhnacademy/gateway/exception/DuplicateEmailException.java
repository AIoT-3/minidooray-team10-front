package com.nhnacademy.gateway.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("[회원가입 실패] 이메일 중복");
    }
}
