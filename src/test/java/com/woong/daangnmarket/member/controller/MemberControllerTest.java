package com.woong.daangnmarket.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woong.daangnmarket.member.dto.LoginRequest;
import com.woong.daangnmarket.member.dto.LoginResponse;
import com.woong.daangnmarket.member.dto.SignUpRequest;
import com.woong.daangnmarket.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공 시 200 반환")
    void signUpSuccess() throws Exception {
        doNothing().when(memberService).signUp(any(SignUpRequest.class));

        SignUpRequest request = new SignUpRequest();
        // setField 또는 생성자 필요
        setField(request, "email", "test@example.com");
        setField(request, "password", "1234abcd");
        setField(request, "nickname", "tester");

        mockMvc.perform(post("/api/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 성공 시 JWT 토큰 반환")
    void loginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        setField(loginRequest, "email", "test@example.com");
        setField(loginRequest, "password", "1234abcd");

        LoginResponse loginResponse = new LoginResponse("jwt-token");

        when(memberService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    // setField 리플렉션 메서드
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
