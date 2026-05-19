package com.nhnacademy.gateway.dto.tag;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TagDeleteRequest {
    private List<Long> tagIds;
}
