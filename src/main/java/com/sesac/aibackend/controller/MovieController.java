package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Movie;
import com.sesac.aibackend.dto.MovieRequest;
import com.sesac.aibackend.dto.MovieResponse;
import com.sesac.aibackend.error.NotFoundException;
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
    private final Map<Long, Movie> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @GetMapping
    public List<MovieResponse> list() {
        return storage.values().stream().map(MovieResponse::from).toList();
    }

    @GetMapping("/{id}")
    public MovieResponse get(@PathVariable Long id) {
        Movie movie = storage.get(id);
        if(movie == null) {
            throw NotFoundException.of("movie", id);
        }
        return MovieResponse.from(movie);
    }

    @PostMapping
    public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest req){
        long id = sequence.getAndIncrement();
        Movie saved = Movie.builder().id(id).genre(req.genre()).title(req.title()).releaseDate(req.releaseDate()).build();
        storage.put(id, saved);
        return ResponseEntity.created(URI.create("/movies/" + id)).body(MovieResponse.from(saved));
    }

    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable Long id, @Valid @RequestBody MovieRequest req) {
        Movie existing = storage.get(id);
        if(existing == null) {
            throw NotFoundException.of("movie", id);
        }
        existing.setGenre(req.genre());
        existing.setTitle(req.title());
        existing.setReleaseDate(req.releaseDate());
        return MovieResponse.from(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if(storage.remove(id) == null) {
            throw NotFoundException.of("movie", id);
        }
        return ResponseEntity.noContent().build();
    }
}
