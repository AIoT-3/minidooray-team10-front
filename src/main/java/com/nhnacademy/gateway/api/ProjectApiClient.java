package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.ErrorResponse;
import com.nhnacademy.gateway.dto.project.ProjectCreateRequest;
import com.nhnacademy.gateway.dto.project.ProjectMemberResponse;
import com.nhnacademy.gateway.dto.project.ProjectModifyRequest;
import com.nhnacademy.gateway.dto.project.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.task.url}")
    private String taskApiUrl;

    /**
     * 프로젝트 리스트 조회 (memberId)
     */
    public List<ProjectResponse> getProjectsByMemberId() {
        return restTemplate.exchange(
                taskApiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProjectResponse>>(){}
        ).getBody();
    }

    /**
     * 프로젝트 생성 요청 (name)
     */
    public void createProjectByName(ProjectCreateRequest request) {
        restTemplate.postForEntity(
                taskApiUrl,
                request,
                Void.class
        );
    }

    /**
     * 프로젝트 단건 조회
     */
    public ProjectResponse geProjectByProjectId(long projectId) {
        return restTemplate.getForObject(
                taskApiUrl + "/" + projectId,
                ProjectResponse.class
        );
    }

    /**
     * 프로젝트 멤버 조회
     */
    public List<ProjectMemberResponse> getProjectMembers(long projectId) {
        return restTemplate.exchange(
                taskApiUrl + "/" + projectId + "/members",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProjectMemberResponse>>(){}
        ).getBody();
    }

    /**
     * 프로젝트 수정 (name)
     */
    public void modifyProjectName(long projectId, ProjectModifyRequest request) {
        restTemplate.put(
                taskApiUrl + "/" + projectId,
                request
        );
    }
}
