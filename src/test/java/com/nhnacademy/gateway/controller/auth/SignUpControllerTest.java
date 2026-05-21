package com.nhnacademy.gateway.controller.auth;

import com.nhnacademy.gateway.api.AccountApiClient;
import com.nhnacademy.gateway.dto.auth.SignUpRequest;
import com.nhnacademy.gateway.exception.account.DuplicateEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignUpController.class)
@AutoConfigureMockMvc(addFilters = false)
class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    AccountApiClient accountApiClient;

    @Test
    @DisplayName("회원 가입 페이지 테스트")
    void signUpPageTest() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signup"));
    }

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void signUpSuccessTest() throws Exception {
        doNothing().when(accountApiClient).signUp(any(SignUpRequest.class));

        mockMvc.perform(post("/signup")
                        .param("email", "test1@test.com")
                        .param("password", "test1234!")
                        .param("name", "test"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));

        verify(accountApiClient, times(1)).signUp(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원 가입 실패 테스트 - validate Fail")
    void signupValidationFailTest() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("email", "test1")
                        .param("password", "test1234!")
                        .param("name", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signup"));


        mockMvc.perform(post("/signup")
                        .param("email", "test1@test.com")
                        .param("password", "test1234")
                        .param("name", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signup"));


        mockMvc.perform(post("/signup")
                        .param("email", "test1@test.com")
                        .param("password", "test1234!")
                        .param("name", "testtesttesttesttesttesttesttesttesttesttesttest"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signup"));
    }

    @Test
    @DisplayName("회원 가입 실패 테스트 - duplicate error")
    void signupDuplicateFailTest() throws Exception {
        doThrow(new DuplicateEmailException()).when(accountApiClient).signUp(any(SignUpRequest.class));

        mockMvc.perform(post("/signup")
                        .param("email", "test1@test.com")
                        .param("password", "test1234!")
                        .param("name", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/signup"))
                .andExpect(model().attribute("errorMsg", "이미 존재하는 Email입니다."));

        verify(accountApiClient, times(1)).signUp(any(SignUpRequest.class));
    }
}
