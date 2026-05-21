package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.ErrorResponse;
import com.nhnacademy.gateway.dto.project.*;
import com.nhnacademy.gateway.exception.ApiException;
import com.nhnacademy.gateway.exception.task.already.MemberAlreadyExistException;
import com.nhnacademy.gateway.exception.task.already.ProjectAlreadyExistException;
import com.nhnacademy.gateway.exception.task.auth.UnauthorizedAccessException;
import com.nhnacademy.gateway.exception.task.notfound.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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
        try {
            restTemplate.postForEntity(
                    taskApiUrl,
                    request,
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse response = parse(e);
            if("P012".equals(response.code())) {
                throw new ProjectAlreadyExistException();
            }
            throw new ApiException(response.status(), response.message());
        }
    }

    /**
     * 프로젝트 단건 조회
     */
    public ProjectResponse getProjectByProjectId(long projectId) {
        try {
            return restTemplate.getForObject(
                    taskApiUrl + "/" + projectId,
                    ProjectResponse.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if("P003".equals(error.code())) {
                throw new ProjectNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }

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
     * 프로젝트 멤버 추가
     */
    public void addProjectMember(long projectId, ProjectAddMemberRequest request) {
        try {
            restTemplate.postForEntity(
                    taskApiUrl + "/" + projectId + "/members",
                    request,
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if("P003".equals(error.code())) {
                throw new ProjectNotFoundException(error.status());
            } else if ("B014".equals(error.code())) {
                throw new MemberAlreadyExistException();
            }
            throw new ApiException(error.status(), error.message());
        }

    }

    /**
     * 프로젝트 멤버 삭제
     */
    public void deleteProjectMember(long projectId, ProjectDeleteMembersRequest request) {
        try {
            HttpEntity<ProjectDeleteMembersRequest> entity = new HttpEntity<>(request);

            restTemplate.exchange(
                    taskApiUrl + "/" + projectId + "/members",
                    HttpMethod.DELETE,
                    entity,
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if("P003".equals(error.code())) {
                throw new UnauthorizedAccessException();
            }
            throw new ApiException(error.status(), error.message());
        }

    }

    /**
     * 프로젝트 수정 (name)
     */
    public void modifyProjectName(long projectId, ProjectModifyRequest request) {
        try {
            restTemplate.put(
                    taskApiUrl + "/" + projectId,
                    request
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if("P003".equals(error.code())) {
                throw new ProjectNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    /**
     * 프로젝트 삭제
     */
    public void deleteProjectById(long projectId) {
        try {
            restTemplate.delete(
                    taskApiUrl + "/" + projectId
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if("P003".equals(error.code())) {
                throw new UnauthorizedAccessException();
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    private ErrorResponse parse(HttpClientErrorException e) {
        try {
            return objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
        } catch (Exception ex) {
            throw e;
        }
    }
}
