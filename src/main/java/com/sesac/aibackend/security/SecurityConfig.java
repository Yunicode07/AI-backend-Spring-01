package com.sesac.aibackend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public

class
SecurityConfig {

    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Http객체 전부
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable) // csrf 꺼버림
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 인증 실패(401) + 권한 부족(403)을 모두 ErrorResponse JSON 표준 포맷으로 통일
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(authenticationEntryPoint) // 403에러 핸들링
                        .accessDeniedHandler(accessDeniedHandler)) // 401에러 핸들링
                .authorizeHttpRequests(auth -> auth // 인증객체
                        // 인증 불필요. /error는 403 에러 포워드가 막혀 401로 덮이지 않도록 개방
                        .requestMatchers( // 요청 부분 매치 (엔드포인트 체크) ** => 와일드 카드 (ALL)
                                "/login", "/signup", "/health",
                                "/oauth2/**", "/login/oauth2/**",
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                "/h2-console/**", "/error"
                        ).permitAll() // 위의 엔드포인트 정책 설정 .permitAll => 허용함
                        // Day 2 인메모리 CRUD 학습용 (운영 권장 X)
                        .requestMatchers("/legacy/items/**").permitAll()
                        // 관리자 전용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 권한이 ADMIN일때만 허용
                        // 그 외 모두 인증 필요 (Day 3 JPA CRUD, /chat 등)
                        .anyRequest().authenticated() // 위의 엔드포인트 외의 모든 요청들은 인증이 필요함
                )
                // H2 콘솔 사용을 위한 헤더 완화 (개발 프로파일만)
                // H2콘솔는 Iframe위에서 사용되지만 security는 Iframe을 사용하기 때문에 허용위해 적용
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                // 구글 OAuth2 로그인 — 성공 시 핸들러가 앱 JWT를 발급 (Day 4 B7/B8)
                // Handler가 분기 처리해줌
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2LoginSuccessHandler))
                // 인가 시작: GET /oauth2/authorization/google, 콜백: /login/oauth2/code/google (자동)
                // 처음엔 .oauth2Login(Customizer.withDefaults()) 이걸로 합니다. 핸들러는 다음 파트에서 진행됩니다.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    // 수동 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
        throws Exception {
        return config.getAuthenticationManager();
    }
}
