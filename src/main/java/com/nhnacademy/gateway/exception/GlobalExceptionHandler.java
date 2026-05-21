package com.nhnacademy.gateway.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public String handleApi(ApiException e, Model model) {
        model.addAttribute("code", e.getStatus());
        model.addAttribute("message", e.getMessage());

        return "error/error";
    }
}
