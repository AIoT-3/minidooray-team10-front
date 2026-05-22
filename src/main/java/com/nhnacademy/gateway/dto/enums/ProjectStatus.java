package com.nhnacademy.gateway.dto.enums;

import java.util.Arrays;

public enum ProjectStatus {
    ACTIVE,
    DORMANT,
    TERMINATE;

    public static ProjectStatus from(String str) {
        return Arrays.stream(values())
                .filter(s -> s.name().equalsIgnoreCase(str))
                .findFirst()
                .orElse(ProjectStatus.ACTIVE);
    }
}
