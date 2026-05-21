package com.nhnacademy.gateway.exception.task.already;

public class MemberAlreadyExistException extends RuntimeException {
    public MemberAlreadyExistException() {
        super("이미 존재하는 멤버입니다.");
    }
}
