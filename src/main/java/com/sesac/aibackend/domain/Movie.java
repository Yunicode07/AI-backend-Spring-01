package com.sesac.aibackend.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    private Long id;

    private String genre;

    private String title;

    private LocalDate releaseDate;
}
