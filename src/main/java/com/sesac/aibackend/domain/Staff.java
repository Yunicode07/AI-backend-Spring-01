package com.sesac.aibackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "staffs")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Staff {

    // 직원 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사번
    @Column(
            unique = true,
            nullable = false,
            length = 10
    )
    private int empNo;

    // 직원 명
    @Column(nullable = false, length = 20)
    private String name;

    // 부서명
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Dept dept;

    // 직책
    @Column(nullable = false, length = 10)
    private String role;

    // 입사일
    @Column(nullable = false, updatable = false)
    private LocalDate hireDate;
}
