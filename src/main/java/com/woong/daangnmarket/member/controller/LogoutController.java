package com.woong.daangnmarket.member.controller;

import com.woong.daangnmarket.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logout")
public class LogoutController {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogoutController.class);

    @PostMapping
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        log.info("[LogoutController] 로그아웃 요청 받음, Authorization 헤더: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("[LogoutController] 잘못된 Authorization 헤더");
            return ResponseEntity.badRequest().body("잘못된 Authorization 헤더입니다.");
        }

        String token = authHeader.substring(7); // Bearer 제거
        log.info("[LogoutController] 토큰 추출됨: {}", token);

        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("[LogoutController] 유효하지 않은 토큰");
            return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
        }

        String email = jwtTokenProvider.getEmail(token);
        long expiration = jwtTokenProvider.getExpiration(token);

        log.info("[LogoutController] 블랙리스트에 토큰 저장 - email: {}, 만료시간(ms): {}", email, expiration);

        // 블랙리스트 등록
        redisTemplate.opsForValue().set(token, email, expiration, TimeUnit.MILLISECONDS);

        log.info("[LogoutController] 로그아웃 처리 완료 - 토큰: {}, 이메일: {}", token, email);

        return ResponseEntity.ok("로그아웃 성공");
    }
}
