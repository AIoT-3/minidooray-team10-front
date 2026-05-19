package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.TagApiClient;
import com.nhnacademy.gateway.dto.tag.TagDeleteRequest;
import com.nhnacademy.gateway.dto.tag.TagCreateRequest;
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
public class TagController {

    private final TagApiClient tagApiClient;

    // TODO-R 공통 처리 (/projects/{project-id}/tags)
    @PostMapping("/projects/{project-id}/tags") // TODO-Q 경로?
    public String tagCreate(@Validated(ValidationSequence.class) @ModelAttribute TagCreateRequest request,
                            BindingResult bindingResult,
                            @PathVariable("project-id") Long projectId) {
        if(bindingResult.hasErrors()) {
            // TODO
            return "/project/projectModify";
        }

        tagApiClient.createTagToProject(projectId, request);

        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/projects/{project-id}/tags/deactivate")
    public String tagDelete(@ModelAttribute TagDeleteRequest request,
                            @PathVariable("project-id") Long projectId) {
        tagApiClient.deleteTags(projectId, request);

        return "redirect:/projects/" + projectId;
    }
}
