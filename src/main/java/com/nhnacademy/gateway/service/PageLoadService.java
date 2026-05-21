package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.api.*;
import com.nhnacademy.gateway.dto.account.request.MemberIdNameRequest;
import com.nhnacademy.gateway.dto.enums.Role;
import com.nhnacademy.gateway.dto.project.ProjectMemberResponse;
import com.nhnacademy.gateway.service.setting.ProjectModifySetting;
import com.nhnacademy.gateway.service.setting.TaskDetailSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * controller에서 페이지 로드 시
 * 세팅 데이터가 많거나 공유가 필요한 경우 service 사용
 */
@Service
@RequiredArgsConstructor
public class PageLoadService {
    // TODO 이렇게 해도 되는건가
    private final TaskApiClient taskApiClient;
    private final CommentApiClient commentApiClient;
    private final ProjectApiClient projectApiClient;
    private final AccountApiClient accountApiClient;
    private final TagApiClient tagApiClient;
    private final MilestoneApiClient milestoneApiClient;

    /**
     * Task Detail Page
     */
    public TaskDetailSetting loadTaskDetail(long projectId, long taskId) {
        return new TaskDetailSetting(
                taskApiClient.getTaskDetail(projectId, taskId),
                commentApiClient.getCommentsByTaskId(projectId, taskId)
        );
    }

    /**
     * Project Modify Page
     */
    public ProjectModifySetting loadProjectModify(long projectId) {
        List<ProjectMemberResponse> projectMembers = projectApiClient.getProjectMembers(projectId);

        Long adminUserId = projectMembers.stream()
                .filter(m -> m.role() == Role.ADMIN)
                .findFirst()
                .orElseThrow()
                .userId();

        return new ProjectModifySetting(
                accountApiClient.getMembersJoinProject(MemberIdNameRequest.from(projectMembers)),
                projectApiClient.getProjectByProjectId(projectId), // project 기본 정보
                tagApiClient.getTagListByProjectId(projectId),
                milestoneApiClient.getMilestoneListByProjectId(projectId),
                adminUserId

        );
    }

}
