package com.sesac.aibackend.service;

import com.sesac.aibackend.util.MessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // Bean 등록할때 쓰는 annotation
@RequiredArgsConstructor // 필수 의존성 주입
public class GreetingService {

    private final MessageFormatter formatter;

    public String hello(String name) {
        return formatter.format(name);
    }
}
