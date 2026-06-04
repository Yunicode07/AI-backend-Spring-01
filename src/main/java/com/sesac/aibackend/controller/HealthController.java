package com.sesac.aibackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController { // 객체 bean으로 만들고 api 컨트롤러 annotation

    @GetMapping("/health2") // /health라는 path로 get Method로 접속되면 수행
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
