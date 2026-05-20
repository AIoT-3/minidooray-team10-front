package com.nhnacademy.gateway.dto.account.response;

import java.util.List;

// List<Id> -> List<Name>
public record MemberListResponse (
        List<MemberIdNameResponse> data
) {
}