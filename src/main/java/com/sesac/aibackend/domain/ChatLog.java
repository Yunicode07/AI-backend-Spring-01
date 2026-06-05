package com.sesac.aibackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // N(ChatLog) : 1(User) / 처음엔 proxy객체로 진행
    @JoinColumn(name = "user_id", nullable = false) // 반드시 JoinColumn이 필요
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String response;

    @CreationTimestamp // 생성시간
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
