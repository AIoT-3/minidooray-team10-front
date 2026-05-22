package com.nhnacademy.gateway.exception.task.notfound;

import com.nhnacademy.gateway.exception.ApiException;

public class ProjectNotFoundException extends ApiException {
    public ProjectNotFoundException(int status) {
        super(status, "존재하지 않는 프로젝트입니다.");
    }
}
