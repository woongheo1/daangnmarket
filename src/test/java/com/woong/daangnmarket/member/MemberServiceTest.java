

// 1. MemberService 단위 테스트
package com.woong.daangnmarket.member;

import com.woong.daangnmarket.jwt.JwtTokenProvider;
import com.woong.daangnmarket.member.domain.Member;
import com.woong.daangnmarket.member.domain.repository.MemberRepository;
import com.woong.daangnmarket.member.dto.LoginRequest;
import com.woong.daangnmarket.member.dto.SignUpRequest;
import com.woong.daangnmarket.member.exception.EmailAlreadyExistsException;
import com.woong.daangnmarket.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private MemberService memberService;

    private SignUpRequest makeSignUpRequest() {
        SignUpRequest request = new SignUpRequest();
        setField(request, "email", "test@example.com");
        setField(request, "password", "1234abcd");
        setField(request, "nickname", "테스터");
        return request;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 회원가입_성공() {
        SignUpRequest request = makeSignUpRequest();

        when(memberRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        memberService.signUp(request);

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void 회원가입_실패_중복이메일() {
        SignUpRequest request = makeSignUpRequest();

        when(memberRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> memberService.signUp(request));
    }

    @Test
    void 로그인_성공() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "1234abcd");
        Member member = Member.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        when(memberRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createToken(member.getEmail())).thenReturn("jwt-token");

        var response = memberService.login(loginRequest);

        assertEquals("jwt-token", response.getToken());

    }

    @Test
    void 로그인_실패_이메일없음() {
        LoginRequest loginRequest = new LoginRequest("notfound@example.com", "1234abcd");

        when(memberRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> memberService.login(loginRequest));
    }

    @Test
    void 로그인_실패_비밀번호불일치() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpass");
        Member member = Member.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        when(memberRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> memberService.login(loginRequest));
    }
}
