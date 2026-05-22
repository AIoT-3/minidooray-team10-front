package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.MilestoneApiClient;
import com.nhnacademy.gateway.api.TagApiClient;
import com.nhnacademy.gateway.api.TaskApiClient;
import com.nhnacademy.gateway.auth.AuthUser;
import com.nhnacademy.gateway.dto.comment.CommentAddTaskRequest;
import com.nhnacademy.gateway.dto.comment.CommentModifyRequest;
import com.nhnacademy.gateway.dto.enums.TaskStatus;
import com.nhnacademy.gateway.dto.milestone.MilestoneResponse;
import com.nhnacademy.gateway.dto.tag.TagResponse;
import com.nhnacademy.gateway.dto.task.TaskCreateRequest;
import com.nhnacademy.gateway.dto.task.TaskDetailResponse;
import com.nhnacademy.gateway.dto.task.TaskModifyRequest;
import com.nhnacademy.gateway.service.PageLoadService;
import com.nhnacademy.gateway.service.setting.TaskDetailSetting;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tasks")
public class TaskController {

    private final TaskApiClient taskApiClient;
    private final TagApiClient tagApiClient;
    private final MilestoneApiClient milestoneApiClient;
    private final PageLoadService pageLoadService;

    @GetMapping
    public String taskCreateForm(@PathVariable("project-id") Long projectId,
                                 Model model) {
        taskCreateLoad(projectId, model);
        model.addAttribute("taskCreateRequest", new TaskCreateRequest());

        return "/task/taskCreate";
    }

    @PostMapping
    public String taskCreate(@PathVariable("project-id") Long projectId,
                             @Validated(ValidationSequence.class) @ModelAttribute TaskCreateRequest request,
                             BindingResult bindingResult,
                             Model model) {
        if(bindingResult.hasErrors()) {
            taskCreateLoad(projectId, model);
            return "task/taskCreate";
        }

        taskApiClient.createTask(projectId, request);

        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{task-id}")
    public String taskDetailForm(@PathVariable("project-id") Long projectId,
                                 @PathVariable("task-id") Long taskId,
                                 Model model,
                                 Authentication authentication) {
        TaskDetailSetting setting = pageLoadService.loadTaskDetail(projectId, taskId);
        Long userId = ((AuthUser) authentication.getPrincipal()).getId();

        model.addAttribute("task", setting.taskDetailResponse());
        model.addAttribute("projectId", projectId);
        model.addAttribute("comments", setting.commentResponses());
        model.addAttribute("loginUserId", userId);

        model.addAttribute("commentAddTaskRequest", new CommentAddTaskRequest());
        return "task/taskDetail";
    }

    @GetMapping("/{task-id}/modify")
    public String taskModifyForm(@PathVariable("project-id") Long projectId,
                                 @PathVariable("task-id") Long taskId,
                                 Model model) {

        taskModifyLoad(projectId, taskId, model);
        return "task/taskModify";
    }

    @PostMapping("/{task-id}/modify")
    public String taskModify(@PathVariable("project-id") Long projectId,
                             @PathVariable("task-id") Long taskId,
                             @Validated(ValidationSequence.class) @ModelAttribute TaskModifyRequest request,
                             BindingResult bindingResult,
                             Model model) {

        if(bindingResult.hasErrors()) {
            taskModifyLoad(projectId, taskId, model);
            return "task/taskModify";
        }
        taskApiClient.modifyTask(projectId, taskId, request);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{task-id}/delete")
    public String taskDelete(@PathVariable("project-id") Long projectId,
                             @PathVariable("task-id") Long taskId) {
        taskApiClient.deleteTask(projectId, taskId);

        return "redirect:/projects/" + projectId;
    }

    private void taskCreateLoad(long projectId, Model model) {
        List<TagResponse> tagResponses = tagApiClient.getTagListByProjectId(projectId);
        List<MilestoneResponse> milestoneResponses = milestoneApiClient.getMilestoneListByProjectId(projectId);

        model.addAttribute("projectId", projectId);
        model.addAttribute("tags", tagResponses);
        model.addAttribute("milestones", milestoneResponses);
    }

    private void taskModifyLoad(long projectId, long taskId, Model model) {
        TaskDetailResponse response = taskApiClient.getTaskDetail(projectId, taskId);
        List<MilestoneResponse> milestones = milestoneApiClient.getMilestoneListByProjectId(projectId);
        List<TagResponse> tags = tagApiClient.getTagListByProjectId(projectId);

        model.addAttribute("projectId", projectId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("taskStatus", TaskStatus.values());
        model.addAttribute("milestones", milestones);
        model.addAttribute("tags", tags);

        model.addAttribute("taskModifyRequest", new TaskModifyRequest(response));
    }
}
