package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.User;
import com.sesac.aibackend.dto.UserRequest;
import com.sesac.aibackend.dto.UserResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // 의존성 주입

    @GetMapping
    public List<UserResponse> list() {
        return userService.findAll().stream().map(UserResponse::from).toList(); // 전체 조회
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        User user = userService.findById(id) // Optional Long Id로 조회
                .orElseThrow(() -> NotFoundException.of("user", id));
        return UserResponse.from(user);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
        // username은 unique 제약이 있으므로, 충돌은 409로 명확히 응답합니다.
        if (userService.existsByUsername(req.username())) { // userName 확인
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "username already exists: " + req.username());
        }
        User saved = userService.save(req.toEntity()); // Entity => username, password, role
        URI location = URI.create("/users/" + saved.getId());
        return ResponseEntity.created(location).body(UserResponse.from(saved)); // user 객체를 dto형태로 내뱉음
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            throw NotFoundException.of("user", id);
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
