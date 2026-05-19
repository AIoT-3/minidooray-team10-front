package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.tag.TagDeleteRequest;
import com.nhnacademy.gateway.dto.tag.TagCreateRequest;
import com.nhnacademy.gateway.dto.tag.TagResponse;
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
public class TagApiClient {
    private final RestTemplate restTemplate;

    @Value("${api.task.url}")
    private String taskApiUrl;

    /**
     * 프로젝트에 속한 태그 리스트 조회
     */
    public List<TagResponse> getTagListByProjectId(long projectId) {
        return restTemplate.exchange(
                taskApiUrl + "/" + projectId + "/tags",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TagResponse>>(){}
        ).getBody();
    }

    /**
     * 프로젝트에 태그 등록
     */
    public void createTagToProject(long projectId, TagCreateRequest tagCreateRequest) {
        restTemplate.postForEntity(
                taskApiUrl + "/" + projectId + "/tags",
                tagCreateRequest,
                Void.class
        );
    }

    /**
     * 프로젝트-태그 삭제
     */
    public void deleteTags(long projectId, TagDeleteRequest request) {
        // delete는 body를 보낼 수 없음
        HttpEntity<TagDeleteRequest> entity = new HttpEntity<>(request);

        restTemplate.exchange(
                taskApiUrl + "/" + projectId + "/tags",
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }
}
