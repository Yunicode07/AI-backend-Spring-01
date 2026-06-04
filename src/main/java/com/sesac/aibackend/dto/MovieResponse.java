package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Movie;

import java.time.LocalDate;

public record MovieResponse(Long id, String genre, String title, LocalDate releaseDate) {
    public static MovieResponse from(Movie movie) {
        return new MovieResponse(movie.getId(), movie.getGenre(), movie.getTitle(), movie.getReleaseDate());
    }
}
