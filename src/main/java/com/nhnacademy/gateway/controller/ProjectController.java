package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.*;
import com.nhnacademy.gateway.auth.AuthUser;
import com.nhnacademy.gateway.dto.account.request.MemberEmailRequest;
import com.nhnacademy.gateway.dto.account.request.MemberIdResponse;
import com.nhnacademy.gateway.dto.enums.ProjectStatus;
import com.nhnacademy.gateway.dto.milestone.MilestoneResponse;
import com.nhnacademy.gateway.dto.project.*;
import com.nhnacademy.gateway.dto.task.TaskResponse;
import com.nhnacademy.gateway.exception.account.MemberInviteFailedException;
import com.nhnacademy.gateway.exception.task.already.MemberAlreadyExistException;
import com.nhnacademy.gateway.exception.task.already.ProjectAlreadyExistException;
import com.nhnacademy.gateway.exception.task.auth.UnauthorizedAccessException;
import com.nhnacademy.gateway.service.PageLoadService;
import com.nhnacademy.gateway.service.setting.ProjectModifySetting;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectApiClient projectApiClient;
    private final AccountApiClient accountApiClient;
    private final MilestoneApiClient milestoneApiClient;
    private final TaskApiClient taskApiClient;
    private final PageLoadService pageLoadService;

    @PostMapping
    public String createProject(@Validated(ValidationSequence.class) @ModelAttribute ProjectCreateRequest request,
                                BindingResult bindingResult,
                                Model model) {

        if(bindingResult.hasErrors()) {
            indexLoad(model);
            return "index";
        }

        try {
            projectApiClient.createProjectByName(request);
        }catch (ProjectAlreadyExistException e) {
            indexLoad(model);
            model.addAttribute("errorMsg", e.getMessage());
            return "index";
        }

        return "redirect:/";
    }

    @GetMapping("/{project-id}")
    public String projectDetail(@PathVariable("project-id") Long projectId,
                                Model model) {

        ProjectResponse projectResponse = projectApiClient.getProjectByProjectId(projectId);
        List<TaskResponse> taskResponses = taskApiClient.getTasksByProjectId(projectId);
        List<MilestoneResponse> milestones = milestoneApiClient.getMilestoneListByProjectId(projectId);

        model.addAttribute("project", projectResponse);
        model.addAttribute("tasks", taskResponses);
        model.addAttribute("milestones", milestones);

        return "project/projectDetail";
    }

    @GetMapping("/{project-id}/modify")
    public String projectModifyForm(@PathVariable("project-id") Long projectId,
                                    Authentication authentication,
                                    Model model) {
        projectModifyLoad(projectId, authentication, model);
        return "project/projectModify";
    }

    @PostMapping("/{project-id}/modify")
    public String projectModify(@PathVariable("project-id") Long projectId,
                                @Validated(ValidationSequence.class) @ModelAttribute ProjectModifyRequest request,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {

        if(bindingResult.hasErrors()) {
            projectModifyLoad(projectId, authentication, model);
            return "project/projectModify";
        }

        projectApiClient.modifyProjectName(projectId, request);

        return "redirect:/projects/" + projectId + "/modify";
    }

    @PostMapping("/{project-id}/delete")
    public String deleteProject(@PathVariable("project-id") Long projectId) {

        projectApiClient.deleteProjectById(projectId);

        return "redirect:/";
    }

    @PostMapping("/{project-id}/members")
    public String memberInviteProject(@PathVariable("project-id") Long projectId,
                                      @ModelAttribute MemberEmailRequest request,
                                      Authentication authentication,
                                      Model model) {
        MemberIdResponse memberIdResponse;
        try {
            memberIdResponse = accountApiClient.getMemberIdByEmail(request); // 추가 가능한 멤버인지 확인
            projectApiClient.addProjectMember(projectId, ProjectAddMemberRequest.from(memberIdResponse)); // 실제 추가
        }catch (MemberInviteFailedException | MemberAlreadyExistException e) {
            projectModifyLoad(projectId, authentication, model);
            model.addAttribute("memberError", e.getMessage());
            return "project/projectModify";
        }


        return "redirect:/projects/" + projectId + "/modify";
    }

    @PostMapping("/{project-id}/members/delete")
    public String memberDeleteProject(@PathVariable("project-id") Long projectId,
                                      @ModelAttribute ProjectDeleteMembersRequest request,
                                      Authentication authentication,
                                      Model model) {
        try {
            projectApiClient.deleteProjectMember(projectId, request);
        }catch (UnauthorizedAccessException e) {
            projectModifyLoad(projectId, authentication, model);
            model.addAttribute("memberError", e.getMessage());
            return "project/projectModify";
        }

        return "redirect:/projects/" + projectId + "/modify";
    }

    private void projectModifyLoad(long projectId, Authentication authentication, Model model) {
        ProjectModifySetting setting = pageLoadService.loadProjectModify(projectId);
        Long userId = ((AuthUser) authentication.getPrincipal()).getId();

        model.addAttribute("setting", setting);
        model.addAttribute("projectStatus", ProjectStatus.values());
        model.addAttribute("loginUserId", userId);

        ProjectModifyRequest modifyRequest = new ProjectModifyRequest();
        modifyRequest.setProjectName(setting.projectResponse().name());
        modifyRequest.setStatus(setting.projectResponse().status());
        model.addAttribute("projectModifyRequest", modifyRequest);
    }

    private void indexLoad(Model model) {
        String name = accountApiClient.getMemberName().name();
        PageResponse<ProjectResponse> projectResponses = projectApiClient.getProjectsByMemberId(ProjectStatus.ACTIVE, 0, 10);

        model.addAttribute("name", name);
        model.addAttribute("projects", projectResponses);
    }

}
