package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Movie;
import com.sesac.aibackend.dto.MovieRequest;
import com.sesac.aibackend.dto.MovieResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieResponse> list() {
        return movieService.list().stream().map(MovieResponse::from).toList();
    }

    @GetMapping("/{id}")
    public MovieResponse get(@PathVariable Long id) {
        Movie movie = movieService.get(id);
        return MovieResponse.from(movie);
    }

    @PostMapping
    public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest req){
        Movie saved = movieService.create(req);
        return ResponseEntity
                .created(URI.create("/movies/" + saved.getId()))
                .body(MovieResponse.from(saved));
    }

    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable Long id, @Valid @RequestBody MovieRequest req) {
        Movie updated = movieService.update(id, req);
        return MovieResponse.from(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
