package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 쿼리메소드 타입은 꼭꼭 확인하고 사용하기
    Optional<User> findByUsername(String username); // 있을수도 있고 없을수도 있고
    
    boolean existsByUsername(String username); // 존재 여부 확인이라 boolean을 사용

    /** 소셜 로그인 신원 조회 — (provider, providerId) 조합이 사용자의 안정적 식별 키입니다. */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
