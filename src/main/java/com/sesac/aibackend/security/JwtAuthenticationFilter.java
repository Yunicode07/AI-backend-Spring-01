package com.sesac.aibackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 모든 요청에서 1회 실행되어 Authorization 헤더의 JWT를 검증합니다.
 *
 * 검증 성공 시 UserDetails 를 principal 로 세팅하여 컨트롤러에서
 * {@code @AuthenticationPrincipal UserDetails user} 로 받을 수 있도록 합니다.
 * (Form 로그인 경로와 principal 타입 일관성을 유지합니다.)
 */
@Slf4j
@Component // 의존성 주입
@RequiredArgsConstructor // 생성단계 의존성 주입
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer "; // 신원인증

    private final JwtUtil jwtUtil; // 만든 생성, 검증 Util
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(HEADER); // 헤더 (요청) 읽음
        if (header != null && header.startsWith(PREFIX)) { // 헤더에 담겨서 왔고, Bearer로 시작하는지
            String token = header.substring(PREFIX.length());
            try {
                Claims claims = jwtUtil.parse(token); // 검증 성공하면
                String username = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // userDetail 생성, Impl 적용

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken( // 토큰으로 만듦
                                userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth); // contextHolder에 넣음
            } catch (JwtException e) {
                log.debug("JWT verification failed: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            } catch (UsernameNotFoundException e) {
                log.debug("user from JWT not found: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
