package com.nhnacademy.gateway.api;

import com.nhnacademy.gateway.dto.ErrorResponse;
import com.nhnacademy.gateway.dto.comment.CommentAddTaskRequest;
import com.nhnacademy.gateway.dto.comment.CommentModifyRequest;
import com.nhnacademy.gateway.dto.comment.CommentResponse;
import com.nhnacademy.gateway.exception.ApiException;
import com.nhnacademy.gateway.exception.task.notfound.CommentNotFoundException;
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
public class CommentApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.task.url}")
    private String taskApiUrl;

    /**
     * 댓글 생성 (Task에)
     */
    public void addCommentToTask(long projectId, long taskId, CommentAddTaskRequest request) {
        try {
            restTemplate.postForEntity(
                    taskApiUrl + "/" + projectId + "/tasks/" + taskId + "/comments",
                    request,
                    Void.class
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
     * 댓글 리스트 조회 (Task)
     */
    public List<CommentResponse> getCommentsByTaskId(long projectId, long taskId) {
        try {
            return restTemplate.exchange(
                    taskApiUrl + "/" + projectId + "/tasks/" + taskId + "/comments",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CommentResponse>>() {
                    }
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("K005".equals(error.code())) {
                throw new TaskNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    /**
     * 댓글 수정
     */
    public void modifyComment(long projectId, long taskId, long CommentId, CommentModifyRequest request) {
        try {
            restTemplate.put(
                    taskApiUrl + "/" + projectId + "/tasks/" + taskId + "/comments/" + CommentId,
                    request
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("C001".equals(error.code())) {
                throw new CommentNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(long projectId, long taskId, long commentId) {
        try {
            restTemplate.delete(
                    taskApiUrl + "/" + projectId + "/tasks/" + taskId + "/comments/" + commentId
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse error = parse(e);
            if ("C001".equals(error.code())) {
                throw new CommentNotFoundException(error.status());
            }
            throw new ApiException(error.status(), error.message());
        }
    }

    // TODO-Q 코드 중복 줄이는 방법
    private ErrorResponse parse(HttpClientErrorException e) {
        try {
            return objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ErrorResponse Parsing 중 오류 발생");
        }
    }
}
