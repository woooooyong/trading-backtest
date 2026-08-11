package com.wooyong.trading_backtest.member;

import com.wooyong.trading_backtest.global.jwt.JwtTokenProvider;
import com.wooyong.trading_backtest.member.dto.LoginRequest;
import com.wooyong.trading_backtest.member.dto.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void signUp(SignUpRequest request){
        if (memberRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = new Member(request.getEmail(), encodedPassword);
        memberRepository.save(member);
    }

    public String login(LoginRequest request) {
        // TODO: memberRepository.findByEmail(...)로 회원 찾기
        Member member = memberRepository.findByEmail(request.getEmail()).orElse(null);

        // TODO: 회원이 없으면 IllegalArgumentException 발생
        if (member == null) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // TODO: passwordEncoder.matches(...)로 비밀번호 비교
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), member.getPassword());
        // TODO: 비밀번호가 다르면 같은 예외 발생

        if (!passwordMatches) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return jwtTokenProvider.generateToken(member);
    }
}