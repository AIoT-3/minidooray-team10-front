package com.nhnacademy.gateway.service.setting;

import com.nhnacademy.gateway.dto.account.response.MemberListResponse;
import com.nhnacademy.gateway.dto.milestone.MilestoneResponse;
import com.nhnacademy.gateway.dto.project.ProjectResponse;
import com.nhnacademy.gateway.dto.tag.TagResponse;

import java.util.List;

public record ProjectModifySetting (
        MemberListResponse memberListResponse,
        ProjectResponse projectResponse,
        List<TagResponse> tagResponses,
        List<MilestoneResponse> milestoneResponses,
        Long adminUserId
){
}
