package com.sesac.aibackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * JWT 발급과 검증.
 *
 * JJWT 0.12.x fluent API 사용:
 * - 빌더: setSubject() 에서 subject() 로 (setter 접두사 제거)
 * - 파서: parserBuilder() 에서 parser() + verifyWith() 로
 * - 결과: parseClaimsJws().getBody() 에서 parseSignedClaims().getPayload() 로
 */
@Component
public class JwtUtil {

    private static final int MIN_SECRET_BYTES = 32;   // HS256 권장 최소 길이

    private final SecretKey secretKey;
    private final long expirationMillis; // 만료시간

    public JwtUtil(
            @Value("${jwt.secret}") String secret, // jwt.secret 속성값 받아옴
            @Value("${jwt.expiration-millis}") long expirationMillis) { // jwt.expiration-millis 토큰 활성화 시간
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "jwt.secret must be at least " + MIN_SECRET_BYTES + " bytes (current: "
                            + bytes.length + "). Set JWT_SECRET environment variable.");
        }
        this.secretKey = Keys.hmacShaKeyFor(bytes);
        this.expirationMillis = expirationMillis;
    }

    // JWT 생성
    public String generate(String username, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username) // 해당 토큰의 이름
                .claim("role", role) // 나머지 속성값 넣고 싶을 때
                .issuedAt(Date.from(now)) // 생성일
                .expiration(Date.from(now.plusMillis(expirationMillis))) // 토큰 만료 시간
                .signWith(secretKey)
                .compact();
    }

    // JWT 검증
    public Claims parse(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
