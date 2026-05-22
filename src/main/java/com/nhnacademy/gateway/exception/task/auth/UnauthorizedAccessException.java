package com.nhnacademy.gateway.exception.task.auth;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("삭제 권한이 없습니다.");
    }
}
