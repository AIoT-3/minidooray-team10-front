package com.nhnacademy.gateway.dto;

public record ErrorResponse (
        int status,
        String code,
        String message
){
}
