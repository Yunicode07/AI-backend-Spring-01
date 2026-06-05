package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.ChatLog;
import com.sesac.aibackend.domain.User;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.ChatLogRepository;
import com.sesac.aibackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatLogService {

    private final UserRepository userRepository;
    private final ChatLogRepository chatLogRepository;

    @Transactional
    public ChatLog save(Long userId, String prompt, String response) { // user객체를 갖고 그대로 저장해야함
        User user = userRepository.findById(userId) // proxy객체로 만들지만 실제 객체가 있어야 한다. (부모 id 꼭 조회해서 넣음)
                .orElseThrow(() -> NotFoundException.of("user", userId));
        return chatLogRepository.save(
                ChatLog.builder()
                        .user(user)
                        .prompt(prompt)
                        .response(response)
                        .build()
        );
    }

    /**
     * 기본 조회 — userId(PK)로 조회, fetch join 없음.
     * 응답에서 getUser().getId()만 읽는 from()과 짝을 이룹니다.
     */
    @Transactional(readOnly = true)
    public List<ChatLog> findByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> NotFoundException.of("user", userId));
        return chatLogRepository.findByUserIdOrderByCreatedAtDesc(userId); // user Id로 찾아서 생성일로 내림차순으로 정렬
    }

    /**
     * fetch join 조회 — userId(PK)로 조회하며 user를 함께 로딩.
     * 응답에서 getUser().getUsername()을 읽는 fromWithUsername()과 짝을 이룹니다.
     */
    @Transactional(readOnly = true)
    public List<ChatLog> findByUserIdWithUser(Long userId) {
        // 존재하지 않는 사용자는 404로 구분 (fetch join은 결과가 없으면 빈 리스트라 구분 불가)
        userRepository.findById(userId)
                .orElseThrow(() -> NotFoundException.of("user", userId));
        return chatLogRepository.findByUserIdWithUser(userId);
    }
}