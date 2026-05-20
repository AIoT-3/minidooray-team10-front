package com.nhnacademy.gateway.dto.comment;

import java.time.ZonedDateTime;

public record CommentResponse (
    Long id,
    long writerId,
    String content,
    ZonedDateTime createdAt
){}