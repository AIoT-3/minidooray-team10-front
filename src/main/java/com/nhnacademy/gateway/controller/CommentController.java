package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.CommentApiClient;
import com.nhnacademy.gateway.dto.comment.CommentAddTaskRequest;
import com.nhnacademy.gateway.dto.comment.CommentModifyRequest;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tasks/{task-id}/comments")
public class CommentController {

    private final CommentApiClient commentApiClient;

    @PostMapping
    public String addCommentByTask(@PathVariable("project-id") Long projectId,
                                   @PathVariable("task-id") Long taskId,
                                   @Validated(ValidationSequence.class) @ModelAttribute CommentAddTaskRequest request,
                                   BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "task/taskDetail";
        }

        commentApiClient.addCommentToTask(projectId, taskId, request);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{comment-id}/modify")
    public String modifyComment(@PathVariable("project-id") Long projectId,
                                @PathVariable("task-id") Long taskId,
                                @PathVariable("comment-id") Long commentId,
                                @Validated(ValidationSequence.class) @ModelAttribute CommentModifyRequest request,
                                BindingResult bindingResult) {
        // TODO 이거 무슨 방법이 있다고 했던 것 같은데 단순화?
        if(bindingResult.hasErrors()) {
            return "/task/taskDetail";
        }

        commentApiClient.modifyComment(projectId, taskId, commentId, request);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;


    }

    @PostMapping("/{comment-id}/delete")
    public String deleteComment(@PathVariable("project-id") Long projectId,
                                @PathVariable("task-id") Long taskId,
                                @PathVariable("comment-id") Long commentId) {

        commentApiClient.deleteComment(projectId, taskId, commentId);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }
}
