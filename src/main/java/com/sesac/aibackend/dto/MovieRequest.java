package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Movie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MovieRequest(
        @NotBlank String genre,
        @NotBlank String title,
        @NotNull LocalDate releaseDate
) {
    public Movie toEntity(){
        return Movie.builder().genre(genre).title(title).releaseDate(releaseDate).build();
    }
}
