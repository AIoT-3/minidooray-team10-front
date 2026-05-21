package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.CommentApiClient;
import com.nhnacademy.gateway.auth.AuthUser;
import com.nhnacademy.gateway.dto.comment.CommentAddTaskRequest;
import com.nhnacademy.gateway.dto.comment.CommentModifyRequest;
import com.nhnacademy.gateway.service.PageLoadService;
import com.nhnacademy.gateway.service.setting.TaskDetailSetting;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final PageLoadService pageLoadService;

    @PostMapping
    public String addCommentByTask(@PathVariable("project-id") Long projectId,
                                   @PathVariable("task-id") Long taskId,
                                   @Validated(ValidationSequence.class) @ModelAttribute CommentAddTaskRequest request,
                                   BindingResult bindingResult,
                                   Authentication authentication,
                                   Model model) {
        if(bindingResult.hasErrors()) {
            taskDetailLoad(projectId, taskId, authentication, model);
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
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {
        if(bindingResult.hasErrors()) {
            // TODO ModifyRequest 수정은 어떻게 해야할지? 객체가 여러개
            taskDetailLoad(projectId, taskId, authentication, model);
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

    private void taskDetailLoad(long projectId, long taskId, Authentication authentication, Model model){
        TaskDetailSetting setting = pageLoadService.loadTaskDetail(projectId, taskId);
        Long userId = ((AuthUser) authentication.getPrincipal()).getId();

        model.addAttribute("task", setting.taskDetailResponse());
        model.addAttribute("projectId", projectId);
        model.addAttribute("comments", setting.commentResponses());
        model.addAttribute("loginUserId", userId);
    }
}
