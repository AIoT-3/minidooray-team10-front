package com.nhnacademy.gateway.exception.task.notfound;

import com.nhnacademy.gateway.exception.ApiException;

public class TagNotFoundException extends ApiException {
    public TagNotFoundException(int status) {
        super(status, "존재하지 않는 태그입니다.");
    }
}
