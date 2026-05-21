package com.nhnacademy.gateway.exception.task.notfound;

import com.nhnacademy.gateway.exception.ApiException;

public class MilestoneNotFoundException extends ApiException {
    public MilestoneNotFoundException(int status) {
        super(status, "존재하지 않는 마일스톤입니다.");
    }
}
