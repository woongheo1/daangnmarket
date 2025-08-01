package com.woong.daangnmarket.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("[JwtAuthenticationFilter] 요청 URI: {}", requestURI);

        String token = resolveToken(request);
        log.info("[JwtAuthenticationFilter] 추출된 토큰: {}", token);

        // 토큰이 존재하고 유효한 경우에만 인증 처리
        if (token != null && jwtTokenProvider.validateToken(token)) {
            log.info("[JwtAuthenticationFilter] 토큰 유효성 검사 통과");

            // 블랙리스트에 등록된 토큰이면 거부
            if (redisTemplate.hasKey(token)) {
                log.warn("[JwtAuthenticationFilter] 블랙리스트 토큰 요청: {}", token);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 인증 객체 설정
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("[JwtAuthenticationFilter] 인증 정보 설정 완료: {}", auth.getName());
        } else {
            // 토큰이 없거나 유효하지 않은 경우, 인증 객체를 설정하지 않고 다음 필터로 진행
            log.info("[JwtAuthenticationFilter] 토큰이 없거나 유효하지 않음 -> 인증 처리 스킵");
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}