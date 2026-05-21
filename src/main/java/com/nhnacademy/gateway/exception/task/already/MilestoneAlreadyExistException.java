package com.nhnacademy.gateway.exception.task.already;

public class MilestoneAlreadyExistException extends RuntimeException {
    public MilestoneAlreadyExistException() {
        super("이미 존재하는 마일스톤입니다.");
    }
}
