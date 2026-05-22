package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.ErrorResponse;
import com.nhnacademy.gateway.dto.tag.TagDeleteRequest;
import com.nhnacademy.gateway.dto.tag.TagCreateRequest;
import com.nhnacademy.gateway.dto.tag.TagResponse;
import com.nhnacademy.gateway.exception.ApiException;
import com.nhnacademy.gateway.exception.task.notfound.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TagApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.task.url}")
    private String taskApiUrl;

    /**
     * 프로젝트에 속한 태그 리스트 조회
     */
    public List<TagResponse> getTagListByProjectId(long projectId) {
        try {
            return restTemplate.exchange(
                    taskApiUrl + "/" + projectId + "/tags",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TagResponse>>() {
                    }
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("P003".equals(error.code())) {
                throw new ProjectNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    /**
     * 프로젝트에 태그 등록
     */
    public void createTagToProject(long projectId, TagCreateRequest tagCreateRequest) {
        try {
            restTemplate.postForEntity(
                    taskApiUrl + "/" + projectId + "/tags",
                    tagCreateRequest,
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("P003".equals(error.code())) {
                throw new ProjectNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    /**
     * 프로젝트-태그 삭제
     */
    public void deleteTags(long projectId, TagDeleteRequest request) {
        try {
            // delete는 body를 보낼 수 없음
            HttpEntity<TagDeleteRequest> entity = new HttpEntity<>(request);

            restTemplate.exchange(
                    taskApiUrl + "/" + projectId + "/tags",
                    HttpMethod.DELETE,
                    entity,
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("P003".equals(error.code())) {
                throw new ProjectNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    private ErrorResponse parse(HttpClientErrorException e) {
        try {
            return objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ErrorResponse Parsing 중 오류 발생");
        }
    }
}
