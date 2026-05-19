package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneDeleteRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MilestoneApiClient {
    private final RestTemplate restTemplate;

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
        restTemplate.postForEntity(
                taskApiUrl + "/" + projectId + "/milestones",
                request,
                Void.class
        );
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
}
