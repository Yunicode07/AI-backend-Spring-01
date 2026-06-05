package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.Item;
import com.sesac.aibackend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 비즈니스 로직을 여기서 사용 (CRUD)
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    // list형 전체 조회
    public List<Item> findAll() {
        return repository.findAll();
    }

    // id값으로 조회
    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }

    // Entity객체 insert or update
    public Item save(Item item) {
        return repository.save(item);
    }

    // Id통해 존재 여부 확인
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    // Id값으로 삭제
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}