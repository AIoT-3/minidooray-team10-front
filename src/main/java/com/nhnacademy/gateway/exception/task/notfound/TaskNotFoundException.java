package com.nhnacademy.gateway.exception.task.notfound;

import com.nhnacademy.gateway.exception.ApiException;

public class TaskNotFoundException extends ApiException {
    public TaskNotFoundException(int status) {
        super(status, "존재하지 않는 업무입니다.");
    }
}
