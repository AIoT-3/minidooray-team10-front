package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.MilestoneApiClient;
import com.nhnacademy.gateway.auth.AuthUser;
import com.nhnacademy.gateway.dto.enums.ProjectStatus;
import com.nhnacademy.gateway.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneDeleteRequest;
import com.nhnacademy.gateway.dto.project.ProjectModifyRequest;
import com.nhnacademy.gateway.exception.task.already.MilestoneAlreadyExistException;
import com.nhnacademy.gateway.service.PageLoadService;
import com.nhnacademy.gateway.service.setting.ProjectModifySetting;
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
@RequestMapping("/projects/{project-id}/milestones")
public class MilestoneController {

    private final MilestoneApiClient milestoneApiClient;
    private final PageLoadService pageLoadService;

    @PostMapping
    public String createMilestones(@PathVariable("project-id") Long projectId,
                                   @Validated(ValidationSequence.class) @ModelAttribute MilestoneCreateRequest request,
                                   BindingResult bindingResult,
                                   Authentication authentication,
                                   Model model) {
        if(bindingResult.hasErrors()) {
            projectModifyLoad(projectId, authentication, model);
            return "project/projectModify";
        }

        try{
            milestoneApiClient.createMilestoneToProject(projectId, request);
        }catch (MilestoneAlreadyExistException e) {
            projectModifyLoad(projectId, authentication, model);
            model.addAttribute("milestoneError", e.getMessage());
            return "project/projectModify";
        }

        return "redirect:/projects/" + projectId + "/modify"; // TODO-S redirect vs view 정리하기
    }

    @PostMapping("/delete")
    public String deleteMilestones(@PathVariable("project-id") Long projectId,
                                   @Validated(ValidationSequence.class) @ModelAttribute MilestoneDeleteRequest request,
                                   BindingResult bindingResult,
                                   Authentication authentication,
                                   Model model) {

        if(bindingResult.hasErrors()) {
            projectModifyLoad(projectId, authentication, model);
            return "project/projectModify";
        }

        milestoneApiClient.deleteMilestones(projectId, request);

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
}
