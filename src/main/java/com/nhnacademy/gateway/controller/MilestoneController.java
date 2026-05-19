package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.MilestoneApiClient;
import com.nhnacademy.gateway.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.milestone.MilestoneDeleteRequest;
import com.nhnacademy.gateway.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneApiClient milestoneApiClient;

    @PostMapping("/projects/{project-id}/milestones")
    public String createMilestones(@PathVariable("project-id") Long projectId,
                                   @Validated(ValidationSequence.class) @ModelAttribute MilestoneCreateRequest request,
                                   BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            // TODO
            return "project/projectModify";
        }

        milestoneApiClient.createMilestoneToProject(projectId, request);

        return "redirect:/projects/" + projectId; // TODO-S redirect vs view 정리하기
    }

    @PostMapping("/projects/{project-id}/milestones/deactivate")
    public String deleteMilestones(@PathVariable("project-id") Long projectId,
                                   @ModelAttribute MilestoneDeleteRequest request) {

        milestoneApiClient.deleteMilestones(projectId, request);

        return "redirect:/projects/" + projectId;
    }
}
