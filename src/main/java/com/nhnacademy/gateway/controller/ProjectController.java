package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.*;
import com.nhnacademy.gateway.auth.AuthUser;
import com.nhnacademy.gateway.dto.account.request.MemberEmailRequest;
import com.nhnacademy.gateway.dto.account.request.MemberIdNameRequest;
import com.nhnacademy.gateway.dto.account.request.MemberIdResponse;
import com.nhnacademy.gateway.dto.account.response.MemberListResponse;
import com.nhnacademy.gateway.dto.enums.ProjectStatus;
import com.nhnacademy.gateway.dto.enums.Role;
import com.nhnacademy.gateway.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneDeleteRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneResponse;
import com.nhnacademy.gateway.dto.project.*;
import com.nhnacademy.gateway.dto.tag.TagCreateRequest;
import com.nhnacademy.gateway.dto.tag.TagDeleteRequest;
import com.nhnacademy.gateway.dto.tag.TagResponse;
import com.nhnacademy.gateway.dto.task.TaskResponse;
import com.nhnacademy.gateway.exception.account.MemberInviteFailedException;
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
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectApiClient projectApiClient;
    private final TagApiClient tagApiClient;
    private final AccountApiClient accountApiClient;
    private final MilestoneApiClient milestoneApiClient;
    private final TaskApiClient taskApiClient;

    @PostMapping
    public String createProject(@Validated(ValidationSequence.class) @ModelAttribute ProjectCreateRequest request,
                                BindingResult bindingResult,
                                Model model) {

        if(bindingResult.hasErrors()) {
            // TODO-R 좀 더 중복을 줄이는 방법 ?
            model.addAttribute("name", accountApiClient.getMemberName().name());

            List<ProjectResponse> projectResponses = projectApiClient.getProjectsByMemberId();
            model.addAttribute("projects", projectResponses);
            return "index";
        }
        projectApiClient.createProjectByName(request);

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
                                    Model model,
                                    Authentication authentication) {

        // TODO-Q 이렇게 controller에서 다 해도 되는건가?
        List<ProjectMemberResponse> projectMembers = projectApiClient.getProjectMembers(projectId);
        MemberListResponse memberListResponse = accountApiClient.getMembersJoinProject(MemberIdNameRequest.from(projectMembers));

        ProjectResponse projectResponse = projectApiClient.getProjectByProjectId(projectId); // project 기본 정보
        ProjectModifyRequest modifyRequest = new ProjectModifyRequest();
        modifyRequest.setProjectName(projectResponse.name());
        modifyRequest.setStatus(projectResponse.status());

        List<TagResponse> tagResponses = tagApiClient.getTagListByProjectId(projectId);
        List<MilestoneResponse> milestoneResponses = milestoneApiClient.getMilestoneListByProjectId(projectId);

        // Response 세팅값
        model.addAttribute("members", memberListResponse.data());
        model.addAttribute("project", projectResponse);
        model.addAttribute("projectStatus", ProjectStatus.values());
        model.addAttribute("tags", tagResponses);
        model.addAttribute("milestones", milestoneResponses);

        Long adminUserId = projectMembers.stream()
                .filter(m -> m.role() == Role.ADMIN)
                .findFirst()
                .orElseThrow()
                .userId();

        Long userId = ((AuthUser) authentication.getPrincipal()).getId();

        model.addAttribute("adminUserId", adminUserId);
        model.addAttribute("loginUserId", userId);

        // request 객체들
        model.addAttribute("projectModify", modifyRequest);
        model.addAttribute("tagCreateRequest", new TagCreateRequest());
        model.addAttribute("tagDeleteRequest", new TagDeleteRequest());
        model.addAttribute("milestoneCreateRequest", new MilestoneCreateRequest());
        model.addAttribute("milestoneDeleteRequest", new MilestoneDeleteRequest());

        return "project/projectModify";
    }

    @PostMapping("/{project-id}/modify")
    public String projectNameModify(@PathVariable("project-id") Long projectId,
                                    @Validated(ValidationSequence.class) @ModelAttribute ProjectModifyRequest request,
                                    BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            // TODO GET 필요한거 Method 분리
            return "project/projectModify";
        }

        projectApiClient.modifyProjectName(projectId, request);

        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{project-id}/delete")
    public String deleteProject(@PathVariable("project-id") Long projectId) {
        projectApiClient.deleteProjectById(projectId);

        return "redirect:/";
    }

    @PostMapping("/{project-id}/members")
    public String memberInviteProject(@PathVariable("project-id") Long projectId,
                                      @ModelAttribute MemberEmailRequest request,
                                      Model model) {

        MemberIdResponse memberIdResponse;

        try {
            memberIdResponse = accountApiClient.getMemberIdByEmail(request); // 추가 가능한 멤버인지
        }catch (MemberInviteFailedException e) {
            // TODO 세팅을..
            model.addAttribute("errorMsg", e.getMessage());
            return "project/projectModify";
        }

        projectApiClient.addProjectMember(projectId, ProjectAddMemberRequest.from(memberIdResponse)); // 실제 추가

        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{project-id}/members/delete")
    public String memberDeleteProject(@PathVariable("project-id") Long projectId,
                                      @ModelAttribute ProjectDeleteMembersRequest request) {
        projectApiClient.deleteProjectMember(projectId, request);

        return "redirect:/projects/" + projectId;
    }

}
