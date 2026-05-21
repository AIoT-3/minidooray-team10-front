package com.nhnacademy.gateway.controller.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그인 실패 테스트 - 로그인 페이지 반환")
    void loginPageTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - error")
    void loginFailErrorTest() throws Exception {
        mockMvc.perform(get("/login")
                        .param("error", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - terminate")
    void loginFailTerminateTest() throws Exception {
        mockMvc.perform(get("/login")
                        .param("terminate", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - dormant")
    void loginFailDormantTest() throws Exception {
        mockMvc.perform(get("/login")
                        .param("dormant", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }
}
