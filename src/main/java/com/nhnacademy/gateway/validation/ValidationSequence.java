package com.nhnacademy.gateway.validation;

import jakarta.validation.GroupSequence;

/**
 * @GroupSequence : 검증 우선순위를 정하는 마커 인터페이스
 * @Valid : 필드에 붙은 검증을 전부 동시에 검사해서 화면에 출력함
 * @Validated()에 설정하여 사용
 */
@GroupSequence({
    NotBlankGroup.class,
    PatternGroup.class
})
public interface ValidationSequence {}