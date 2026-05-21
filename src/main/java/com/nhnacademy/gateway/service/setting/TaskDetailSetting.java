package com.nhnacademy.gateway.service.setting;

import com.nhnacademy.gateway.dto.comment.CommentResponse;
import com.nhnacademy.gateway.dto.task.TaskDetailResponse;
import java.util.List;

public record TaskDetailSetting (
        TaskDetailResponse taskDetailResponse,
        List<CommentResponse> commentResponses
){
}
