package com.sesac.aibackend.controller;

import com.sesac.aibackend.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GreetingController {
    // final은 의존성 주입 받아오는 객체
    private final GreetingService greetingService;

    @GetMapping("/greeting")
    public Map<String, String> greeting(
            @RequestParam(defaultValue = "World")String name) {
        // service에 있는 hello 가져옴
        return Map.of("message", greetingService.hello(name));
    }
}
