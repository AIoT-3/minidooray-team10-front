package com.nhnacademy.gateway.exception.task.notfound;

import com.nhnacademy.gateway.exception.ApiException;

public class CommentNotFoundException extends ApiException {
    public CommentNotFoundException(int status) {
        super(status, "존재하지 않는 댓글입니다.");
    }
}
