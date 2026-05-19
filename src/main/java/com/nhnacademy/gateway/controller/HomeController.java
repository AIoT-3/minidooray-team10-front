package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.api.ProjectApiClient;
import com.nhnacademy.gateway.dto.project.ProjectCreateRequest;
import com.nhnacademy.gateway.dto.project.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final AccountApiClient accountApiClient;
    private final ProjectApiClient projectApiClient;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("name", accountApiClient.getMemberName().name());

        List<ProjectResponse> projectResponses = projectApiClient.getProjectsByMemberId();
        model.addAttribute("projects", projectResponses);

        model.addAttribute("projectCreateRequest", new ProjectCreateRequest());
        return "index";
    }
}