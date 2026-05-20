package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.TagApiClient;
import com.nhnacademy.gateway.dto.tag.TagDeleteRequest;
import com.nhnacademy.gateway.dto.tag.TagCreateRequest;
import com.nhnacademy.gateway.validation.ValidationSequence;
import jakarta.validation.Valid;
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
@RequestMapping("/projects/{project-id}/tags")
public class TagController {

    private final TagApiClient tagApiClient;

    @PostMapping // TODO-Q 경로?
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
// task-tag : deactivate
    @PostMapping("/delete")
    public String tagDelete(@Valid @ModelAttribute TagDeleteRequest request,
                            BindingResult bindingResult,
                            @PathVariable("project-id") Long projectId) {
        if(bindingResult.hasErrors()) {

            return "project/projectModify";
        }

        tagApiClient.deleteTags(projectId, request);

        return "redirect:/projects/" + projectId;
    }
}
