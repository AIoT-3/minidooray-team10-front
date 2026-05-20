package com.nhnacademy.gateway.dto.account.request;

import com.nhnacademy.gateway.dto.project.ProjectMemberResponse;

import java.util.List;

public record MemberIdNameRequest(
        List<Long> ids
){
    public static MemberIdNameRequest from(List<ProjectMemberResponse> projectMemberResponses) {
        return new MemberIdNameRequest(
                projectMemberResponses.stream()
                        .map(ProjectMemberResponse::userId).toList()
        );
    }
}