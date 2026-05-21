package com.nhnacademy.gateway.converter;

import com.nhnacademy.gateway.dto.enums.ProjectStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToProjectStatusConverter implements Converter<String, ProjectStatus> {
    @Override
    public ProjectStatus convert(String source) {
        return ProjectStatus.from(source);
    }
}
