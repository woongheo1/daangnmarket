package com.woong.daangnmarket.member.controller;

import com.woong.daangnmarket.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(LogoutController.class)
public class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RedisTemplate<String, String> redisTemplate;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그아웃 성공 - 유효한 토큰")
    void logoutSuccess() throws Exception {
        String token = "valid.jwt.token";
        String authHeader = "Bearer " + token;
        String email = "test@example.com";
        long expirationMillis = 1000L * 60 * 60; // 1시간

        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmail(token)).thenReturn(email);
        when(jwtTokenProvider.getExpiration(token)).thenReturn(expirationMillis);
        doNothing().when(redisTemplate.opsForValue()).set(token, email, expirationMillis, TimeUnit.MILLISECONDS);

        mockMvc.perform(post("/api/logout")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃 성공"));

        verify(redisTemplate.opsForValue(), times(1)).set(token, email, expirationMillis, TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("로그아웃 실패 - Authorization 헤더 없음")
    void logoutFail_NoAuthorizationHeader() throws Exception {
        mockMvc.perform(post("/api/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("잘못된 Authorization 헤더입니다."));
    }

    @Test
    @DisplayName("로그아웃 실패 - 토큰 유효하지 않음")
    void logoutFail_InvalidToken() throws Exception {
        String token = "invalid.jwt.token";
        String authHeader = "Bearer " + token;

        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        mockMvc.perform(post("/api/logout")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("유효하지 않은 토큰입니다."));
    }
}
