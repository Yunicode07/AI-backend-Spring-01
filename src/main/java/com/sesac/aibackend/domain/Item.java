package com.sesac.aibackend.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 빈 생성자
@AllArgsConstructor // 전체 argument 생성자
@Builder
public class Item {

    private Long id;
    private String name;
    private int price;
}
