package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.comment.CommentAddTaskRequest;
import com.nhnacademy.gateway.dto.comment.CommentModifyRequest;
import com.nhnacademy.gateway.dto.comment.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentApiClient {
    private final RestTemplate restTemplate;

    @Value("${api.task.url}")
    private String taskApiUrl;

    /**
     * 댓글 생성 (Task에)
     */
    public void addCommentToTask(long projectId, long taskId, CommentAddTaskRequest request) {
        restTemplate.postForEntity(
                taskApiUrl + "/" + projectId + "/tasks/" + taskId +  "/comments",
                request,
                Void.class
        );
    }

    /**
     * 댓글 리스트 조회 (Task)
     */
    public List<CommentResponse> getCommentsByTaskId(long projectId, long taskId) {
        return restTemplate.exchange(
                taskApiUrl + "/" + projectId + "/tasks/" + taskId + "/comments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CommentResponse>>() {}
        ).getBody();
    }

    /**
     * 댓글 수정
     */
    public void modifyComment(long projectId, long taskId, long CommentId, CommentModifyRequest request) {
        restTemplate.put(
                taskApiUrl + "/" + projectId + "/tasks/" + taskId + "/comments/" + CommentId,
                request
        );
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(long projectId, long taskId, long commentId) {
        restTemplate.delete(
                taskApiUrl + "/" + projectId + "/tasks/" + taskId + "/comments/" + commentId
        );
    }
}
