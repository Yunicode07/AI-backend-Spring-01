package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.Movie;
import com.sesac.aibackend.dto.MovieRequest;
import com.sesac.aibackend.dto.MovieResponse;
import com.sesac.aibackend.error.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final Map<Long, Movie> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<Movie> list() {
        return storage.values().stream().toList();
    }

    public Movie get(Long id) {
        Movie movie = storage.get(id);
        if(movie == null) {
            throw NotFoundException.of("movie", id);
        }
        return movie;
    }

    public Movie create(MovieRequest req){
        long id = sequence.getAndIncrement();
        Movie saved = Movie.builder()
                .id(id)
                .genre(req.genre())
                .title(req.title())
                .releaseDate(req.releaseDate())
                .build();
        storage.put(id, saved);
        return saved;
    }

    public Movie update(Long id, MovieRequest req) {
        Movie existing = storage.get(id);
        if(existing == null) {
            throw NotFoundException.of("movie", id);
        }
        existing.setGenre(req.genre());
        existing.setTitle(req.title());
        existing.setReleaseDate(req.releaseDate());
        return existing;
    }

    public void delete(Long id) {
        if(storage.remove(id) == null) {
            throw NotFoundException.of("movie", id);
        }
    }
}
