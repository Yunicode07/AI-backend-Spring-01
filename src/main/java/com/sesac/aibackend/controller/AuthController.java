package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Role;
import com.sesac.aibackend.domain.User;
import com.sesac.aibackend.dto.LoginRequest;
import com.sesac.aibackend.dto.SignupRequest;
import com.sesac.aibackend.error.DuplicateException;
import com.sesac.aibackend.repository.UserRepository;
import com.sesac.aibackend.security.JwtUtil;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 컨트롤러 — /signup, /login.
 *
 * /login 성공 시 JWT를 발급하여 응답합니다.
 *
 * 동시성 주의: existsByUsername 후 save 는 TOCTOU race 위험.
 * users.username UNIQUE 제약과 DataIntegrityViolationException 잡기로 이중 방어합니다.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody SignupRequest req) {
        if (userRepository.existsByUsername(req.username())) { // 사용자가 이미 있으면
            throw new DuplicateException("username already taken: " + req.username()); // 중복 에러
        }
        try { // 중복된 사용자가 아니면
            userRepository.save(User.builder() // 사용자 등록
                    .username(req.username())
                    .passwordHash(passwordEncoder.encode(req.password())) // BCrypt
                    .role(Role.USER)
                    .build());
        } catch (DataIntegrityViolationException e) {
            // unique 제약 위반 — 동시 가입 시도
            throw new DuplicateException("username already taken: " + req.username());
        }// 에러도 안나고 save도 정상적으로 되면 201 출력 후 종료
        return ResponseEntity.status(201).body(Map.of("username", req.username()));
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest req) {
        // security에서 인증관리를 담당 authenticationManager
        Authentication auth = authenticationManager.authenticate( // 검증 단계 -> 내부적으로는 UserDetailsService의 loadUserByUsername 동작
                new UsernamePasswordAuthenticationToken(req.username(), req.password()) // 맞으면 인증객체 생성
        );
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority) // 인증객체 꺼내오고
                .map(a -> a.replace("ROLE_", ""))
                .orElse("USER"); // DB에 User = "ADMIN"으로 저장하지만 security 내부는 "ROLE_"로 관리
        String token = jwtUtil.generate(auth.getName(), role); // 성공하면 JWT 토큰 내보냄
        return Map.of("token", token, "username", auth.getName(), "role", role);
    }
}
