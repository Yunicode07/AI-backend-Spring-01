package com.sesac.aibackend.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    private Long id;

    private String genre;

    private String title;

    private Date releaseDate;
}
