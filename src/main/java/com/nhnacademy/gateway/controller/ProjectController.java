package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.api.MilestoneApiClient;
import com.nhnacademy.gateway.api.ProjectApiClient;
import com.nhnacademy.gateway.api.TagApiClient;
import com.nhnacademy.gateway.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneResponse;
import com.nhnacademy.gateway.dto.project.ProjectCreateRequest;
import com.nhnacademy.gateway.dto.project.ProjectMemberResponse;
import com.nhnacademy.gateway.dto.project.ProjectModifyRequest;
import com.nhnacademy.gateway.dto.project.ProjectResponse;
import com.nhnacademy.gateway.dto.tag.TagCreateRequest;
import com.nhnacademy.gateway.dto.tag.TagResponse;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.zip.Adler32;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectApiClient projectApiClient;
    private final TagApiClient tagApiClient;
    private final AccountApiClient accountApiClient;
    private final MilestoneApiClient milestoneApiClient;

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
    public String projectModifyForm(@PathVariable("project-id") Long projectId,
                                    Model model) {

        // TODO-Q 이렇게 controller에서 다 해도 되는건가?
        List<ProjectMemberResponse> projectMembers = projectApiClient.getProjectMembers(projectId);


        ProjectResponse projectResponse = projectApiClient.geProjectByProjectId(projectId); // project 기본 정보
        ProjectModifyRequest modifyRequest = new ProjectModifyRequest();
        modifyRequest.setProjectName(projectResponse.name());

        List<TagResponse> tagResponses = tagApiClient.getTagListByProjectId(projectId);
        List<MilestoneResponse> milestoneResponses = milestoneApiClient.getMilestoneListByProjectId(projectId);

        model.addAttribute("project", projectResponse);
        model.addAttribute("projectNameModify", modifyRequest);
        model.addAttribute("tags", tagResponses);
        model.addAttribute("tagCreateRequest", new TagCreateRequest());
        model.addAttribute("milestones", milestoneResponses);
        model.addAttribute("milestoneCreateRequest", new MilestoneCreateRequest());

        return "project/projectModify";
    }

    @PostMapping("/{project-id}")
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


}
