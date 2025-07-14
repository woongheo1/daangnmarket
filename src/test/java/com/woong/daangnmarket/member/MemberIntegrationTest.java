package com.woong.daangnmarket.member;

import com.woong.daangnmarket.member.dto.LoginRequest;
import com.woong.daangnmarket.member.dto.LoginResponse;
import com.woong.daangnmarket.member.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void 회원가입_로그인_로그아웃_인증실패_통합() {
        // 1. 회원가입
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("1234abcd");
        signUpRequest.setNickname("테스터");

        ResponseEntity<Void> signUpResponse = restTemplate.postForEntity("/api/members/signup", signUpRequest, Void.class);
        assertThat(signUpResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 2. 로그인
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("1234abcd");

        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("/api/members/login", loginRequest, LoginResponse.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = loginResponse.getBody().getToken();
        assertThat(token).isNotNull();

        // 3. 로그아웃
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> logoutRequest = new HttpEntity<>(headers);

        ResponseEntity<String> logoutResponse = restTemplate.exchange("/api/logout", HttpMethod.POST, logoutRequest, String.class);
        assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(logoutResponse.getBody()).contains("로그아웃 성공");

        // 4. 로그아웃 후, 같은 토큰으로 보호된 API 호출 시 인증 실패 예상 (예: 내 프로필 조회)
        ResponseEntity<String> profileResponse = restTemplate.exchange("/api/members/my-profile", HttpMethod.GET, logoutRequest, String.class);
        assertThat(profileResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
