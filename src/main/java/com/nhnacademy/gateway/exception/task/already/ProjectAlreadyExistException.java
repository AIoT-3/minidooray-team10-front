package com.nhnacademy.gateway.exception.task.already;

public class ProjectAlreadyExistException extends RuntimeException {
    public ProjectAlreadyExistException() {
        super("이미 존재하는 프로젝트입니다.");
    }
}
