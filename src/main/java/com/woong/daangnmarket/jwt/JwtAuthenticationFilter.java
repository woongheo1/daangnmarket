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

        // 로그아웃 경로는 필터 통과시키기 (블랙리스트 검사 생략)
        if (requestURI.equals("/api/logout")) {
            log.info("[JwtAuthenticationFilter] 로그아웃 경로, 필터 통과");
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);
        log.info("[JwtAuthenticationFilter] 추출된 토큰: {}", token);

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
            log.info("[JwtAuthenticationFilter] 토큰이 없거나 유효하지 않음");
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}

