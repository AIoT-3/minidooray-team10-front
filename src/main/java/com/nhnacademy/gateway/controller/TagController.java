package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.TagApiClient;
import com.nhnacademy.gateway.auth.AuthUser;
import com.nhnacademy.gateway.dto.enums.ProjectStatus;
import com.nhnacademy.gateway.dto.tag.TagDeleteRequest;
import com.nhnacademy.gateway.dto.tag.TagCreateRequest;
import com.nhnacademy.gateway.service.PageLoadService;
import com.nhnacademy.gateway.service.setting.ProjectModifySetting;
import com.nhnacademy.gateway.validation.ValidationSequence;
import jakarta.validation.Valid;
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
@RequestMapping("/projects/{project-id}/tags")
public class TagController {

    private final TagApiClient tagApiClient;
    private final PageLoadService pageLoadService;

    @PostMapping
    public String tagCreate(@PathVariable("project-id") Long projectId,
                            @Validated(ValidationSequence.class) @ModelAttribute TagCreateRequest request,
                            BindingResult bindingResult,
                            Authentication authentication,
                            Model model) {
        if(bindingResult.hasErrors()) {
            projectModifyLoad(projectId, authentication, model);
            return "/project/projectModify";
        }

        tagApiClient.createTagToProject(projectId, request);

        return "redirect:/projects/" + projectId + "/modify";
    }

    @PostMapping("/delete")
    public String tagDelete(@PathVariable("project-id") Long projectId,
                            @Valid @ModelAttribute TagDeleteRequest request,
                            BindingResult bindingResult,
                            Authentication authentication,
                            Model model) {
        if(bindingResult.hasErrors()) {
            projectModifyLoad(projectId, authentication, model);
            return "project/projectModify";
        }

        tagApiClient.deleteTags(projectId, request);

        return "redirect:/projects/" + projectId + "/modify";
    }

// task-tag : deactivate

    private void projectModifyLoad(long projectId, Authentication authentication, Model model) {
        ProjectModifySetting setting = pageLoadService.loadProjectModify(projectId);
        Long adminUserId = setting.adminUserId();
        Long userId = ((AuthUser) authentication.getPrincipal()).getId();

        model.addAttribute("members", setting.memberListResponse().data());
        model.addAttribute("project", setting.projectResponse());
        model.addAttribute("projectStatus", ProjectStatus.values());
        model.addAttribute("tags", setting.tagResponses());
        model.addAttribute("milestones", setting.milestoneResponses());
        model.addAttribute("adminUserId", adminUserId);
        model.addAttribute("loginUserId", userId);
    }
}
