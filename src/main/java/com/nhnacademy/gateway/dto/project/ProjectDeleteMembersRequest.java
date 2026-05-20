package com.nhnacademy.gateway.dto.project;

import java.util.List;

public record ProjectDeleteMembersRequest(
        List<Long> userIds
) {}
