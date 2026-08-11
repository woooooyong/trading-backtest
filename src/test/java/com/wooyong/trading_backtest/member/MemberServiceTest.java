package com.wooyong.trading_backtest.member;
import com.wooyong.trading_backtest.global.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wooyong.trading_backtest.member.dto.SignUpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.wooyong.trading_backtest.member.dto.LoginRequest;

import java.util.Optional;

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

    @Test
    void 이미_사용중인_이메일이면_회원가입에_실패한다() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(memberRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> memberService.signUp(request)
        );

        verifyNoInteractions(passwordEncoder);

        verify(memberRepository, never())
                .save(any(Member.class));
    }


    @Test
    void 정상_입력이면_암호화된_비밀번호로_회원을_저장한다() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(memberRepository.existsByEmail("new@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        memberService.signUp(request);

        ArgumentCaptor<Member> memberCaptor =
                ArgumentCaptor.forClass(Member.class);

        verify(memberRepository).save(memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();

        assertEquals("new@example.com", savedMember.getEmail());
        assertEquals("encoded-password", savedMember.getPassword());
    }

    @Test
    void 이메일과_비밀번호가_맞으면_JWT_토큰을_반환한다() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        Member member = new Member("test@example.com", "encoded-password");

        when(memberRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(member));

        when(passwordEncoder.matches("password123", "encoded-password"))
                .thenReturn(true);

        when(jwtTokenProvider.generateToken(member))
                .thenReturn("test-token");

        String token = memberService.login(request);

        assertEquals("test-token", token);
    }

    @Test
    void 비밀번호가_틀리면_로그인에_실패한다() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong-password");

        Member member = new Member("test@example.com", "encoded-password");

        when(memberRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(member));

        when(passwordEncoder.matches("wrong-password", "encoded-password"))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> memberService.login(request)
        );

        verify(jwtTokenProvider, never()).generateToken(any(Member.class));
    }

    @Test
    void 존재하지_않는_이메일이면_로그인에_실패한다() {
        LoginRequest request = new LoginRequest();
        request.setEmail("nobody@example.com");
        request.setPassword("password123");

        when(memberRepository.findByEmail("nobody@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> memberService.login(request)
        );

        verifyNoInteractions(passwordEncoder, jwtTokenProvider);
    }
}