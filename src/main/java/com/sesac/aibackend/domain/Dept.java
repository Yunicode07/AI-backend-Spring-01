package com.sesac.aibackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dept")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Dept {

    // 부서 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 부서명
    @Column(nullable = false, length = 100)
    private String deptname;

    // 인원수
    @Column(nullable = false, length = 3)
    private int personCount;

}
