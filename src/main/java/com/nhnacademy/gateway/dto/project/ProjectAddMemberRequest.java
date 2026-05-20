package com.nhnacademy.gateway.dto.project;

import com.nhnacademy.gateway.dto.account.request.MemberIdResponse;

public record ProjectAddMemberRequest (
        Long userId
) {
    public static ProjectAddMemberRequest from(MemberIdResponse response) {
        return new ProjectAddMemberRequest(response.id());
    }
}
