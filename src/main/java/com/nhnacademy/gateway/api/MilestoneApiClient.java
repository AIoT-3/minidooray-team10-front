package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.ErrorResponse;
import com.nhnacademy.gateway.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneDeleteRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneResponse;
import com.nhnacademy.gateway.exception.ApiException;
import com.nhnacademy.gateway.exception.task.already.MilestoneAlreadyExistException;
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
public class MilestoneApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.task.url}")
    private String taskApiUrl;

    /**
     * 프로젝트에 등록된 마일스톤 리스트 조회
     */
    public List<MilestoneResponse> getMilestoneListByProjectId(long projectId) {
        return restTemplate.exchange(
                taskApiUrl + "/" + projectId + "/milestones",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MilestoneResponse>>(){}
        ).getBody();
    }

    /**
     * 프로젝트에 마을스톤 추가
     */
    public void createMilestoneToProject(long projectId, MilestoneCreateRequest request) {
        try {
            restTemplate.postForEntity(
                    taskApiUrl + "/" + projectId + "/milestones",
                    request,
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse response = parse(e);
            if ("L011".equals(response.code())) {
                throw new MilestoneAlreadyExistException();
            }
            throw new ApiException(response.status(), response.message());
        }

    }

    /**
     * 프로젝트에 등록된 마일스톤 삭제
     */
    public void deleteMilestones(long projectId, MilestoneDeleteRequest request) {
        HttpEntity<MilestoneDeleteRequest> entity = new HttpEntity<>(request);
        restTemplate.exchange(
                taskApiUrl + "/" + projectId + "/milestones",
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }

    private ErrorResponse parse(HttpClientErrorException e) {
        try {
            return objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
        } catch (Exception ex) {
            throw e;
        }
    }
}
