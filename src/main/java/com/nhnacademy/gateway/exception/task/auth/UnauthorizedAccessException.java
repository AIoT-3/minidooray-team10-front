package com.nhnacademy.gateway.exception.task.auth;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("접근 권한이 없습니다.");
    }
}
