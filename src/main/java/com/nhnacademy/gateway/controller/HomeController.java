package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.api.ProjectApiClient;
import com.nhnacademy.gateway.dto.enums.ProjectStatus;
import com.nhnacademy.gateway.dto.project.PageResponse;
import com.nhnacademy.gateway.dto.project.ProjectCreateRequest;
import com.nhnacademy.gateway.dto.project.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final AccountApiClient accountApiClient;
    private final ProjectApiClient projectApiClient;

    @GetMapping
    public String home(@RequestParam(name = "status", defaultValue = "active") ProjectStatus status,
                       Model model) {

        PageResponse<ProjectResponse> projectResponses = projectApiClient.getProjectsByMemberId(status);

        model.addAttribute("name", accountApiClient.getMemberName().name());
        model.addAttribute("projects", projectResponses);
        model.addAttribute("status", status.name().toLowerCase());

        model.addAttribute("projectCreateRequest", new ProjectCreateRequest());
        return "index";
    }
}