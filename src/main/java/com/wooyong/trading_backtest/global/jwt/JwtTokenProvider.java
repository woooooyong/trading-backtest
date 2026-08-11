package com.wooyong.trading_backtest.global.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import com.wooyong.trading_backtest.member.Member;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import io.jsonwebtoken.JwtException;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationMilliseconds;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-milliseconds}") long expirationMilliseconds
    ) {
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
        this.expirationMilliseconds = expirationMilliseconds;
    }

    public String generateToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(
                now.getTime() + expirationMilliseconds
        );

        // TODO: Jwts.builder()로 토큰 생성
        return Jwts.builder()
                .subject(member.getEmail())
                .claim("memberId", member.getId())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}