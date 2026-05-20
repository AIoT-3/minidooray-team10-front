package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.task.TaskCreateRequest;
import com.nhnacademy.gateway.dto.task.TaskDetailResponse;
import com.nhnacademy.gateway.dto.task.TaskModifyRequest;
import com.nhnacademy.gateway.dto.task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskApiClient {
    private final RestTemplate restTemplate;

    @Value("${api.task.url}")
    private String taskApiUrl;

    /**
     * Task 생성
     */
    public void createTask(long projectId, TaskCreateRequest request) {
        // TODO Response로 projectId를 주는데 필요 없을 것 같은데 ?
        restTemplate.postForEntity(
                taskApiUrl + "/" + projectId +"/tasks",
                request,
                Void.class
        );
    }

    /**
     * Project에 속한 Task List 조회
     */
    public List<TaskResponse> getTasksByProjectId(long projectId) {
        return restTemplate.exchange(
                taskApiUrl + "/" + projectId + "/tasks",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TaskResponse>>() {}
        ).getBody();
    }

    /**
     * Task 상세 조회
     */
    public TaskDetailResponse getTaskDetail(long projectId, long taskId) {
        return restTemplate.getForObject(
                taskApiUrl + "/" + projectId + "/tasks/" + taskId,
                TaskDetailResponse.class
        );
    }

    /**
     * Task 수정
     */
    public void modifyTask(long projectId, long taskId, TaskModifyRequest request) {
        restTemplate.put(
                taskApiUrl + "/" + projectId + "/tasks/" + taskId,
                request
        );
    }

    /**
     * Task 삭제
     */
    public void deleteTask(long projectId, long taskId) {
        restTemplate.delete(
                taskApiUrl + "/" + projectId + "/tasks/" + taskId
        );
    }
}
