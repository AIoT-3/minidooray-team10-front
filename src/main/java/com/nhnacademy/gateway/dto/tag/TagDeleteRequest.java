package com.nhnacademy.gateway.dto.tag;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TagDeleteRequest {
    @NotNull(message = "삭제하고 싶은 tag를 한 개 이상 선택해주세요.")
    private List<Long> tagIds;
}
