package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.ErrorResponse;
import com.nhnacademy.gateway.dto.task.TaskCreateRequest;
import com.nhnacademy.gateway.dto.task.TaskDetailResponse;
import com.nhnacademy.gateway.dto.task.TaskModifyRequest;
import com.nhnacademy.gateway.dto.task.TaskResponse;
import com.nhnacademy.gateway.exception.ApiException;
import com.nhnacademy.gateway.exception.task.notfound.MilestoneNotFoundException;
import com.nhnacademy.gateway.exception.task.notfound.ProjectNotFoundException;
import com.nhnacademy.gateway.exception.task.notfound.TagNotFoundException;
import com.nhnacademy.gateway.exception.task.notfound.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.task.url}")
    private String taskApiUrl;

    /**
     * Task 생성
     */
    public void createTask(long projectId, TaskCreateRequest request) {
        try {
            restTemplate.postForEntity(
                    taskApiUrl + "/" + projectId + "/tasks",
                    request,
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("P003".equals(error.code())) {
                throw new ProjectNotFoundException(error.status());
            } else if ("L002".equals(error.code())) {
                throw new MilestoneNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }

    }

    /**
     * Project에 속한 Task List 조회
     */
    public List<TaskResponse> getTasksByProjectId(long projectId) {
        try {
            return restTemplate.exchange(
                    taskApiUrl + "/" + projectId + "/tasks",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TaskResponse>>() {
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
     * Task 상세 조회
     */
    public TaskDetailResponse getTaskDetail(long projectId, long taskId) {
        try {
            return restTemplate.getForObject(
                    taskApiUrl + "/" + projectId + "/tasks/" + taskId,
                    TaskDetailResponse.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("K005".equals(error.code())) {
                throw new TaskNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    /**
     * Task 수정
     */
    public void modifyTask(long projectId, long taskId, TaskModifyRequest request) {
        try {
            restTemplate.put(
                    taskApiUrl + "/" + projectId + "/tasks/" + taskId,
                    request
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("K005".equals(error.code())) {
                throw new TaskNotFoundException(error.status());
            } else if ("L002".equals(error.code())) {
                throw new MilestoneNotFoundException(error.status());
            } else if ("T004".equals(error.code())) {
                throw new TagNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    /**
     * Task 삭제
     */
    public void deleteTask(long projectId, long taskId) {
        try {
            restTemplate.delete(
                    taskApiUrl + "/" + projectId + "/tasks/" + taskId
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("K005".equals(error.code())) {
                throw new TaskNotFoundException(error.status());
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
