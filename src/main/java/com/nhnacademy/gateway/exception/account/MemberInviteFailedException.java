package com.nhnacademy.gateway.exception.account;

public class MemberInviteFailedException extends RuntimeException {
    public MemberInviteFailedException() {
        super("[멤버추가실패] 이메일을 다시 확인해주세요.");
    }
}
