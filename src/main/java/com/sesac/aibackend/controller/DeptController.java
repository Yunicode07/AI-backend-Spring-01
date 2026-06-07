package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Dept;
import com.sesac.aibackend.dto.DeptRequest;
import com.sesac.aibackend.dto.DeptResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.service.DeptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    // 전체 부서 조회
    @GetMapping
    public List<DeptResponse> findAll() {
        return deptService.findAll()
                .stream()
                .map(DeptResponse::from)
                .toList();
    }

    // 부서 단건 조회
    @GetMapping("/{id}")
    public DeptResponse findById(@PathVariable Long id) {
        Dept dept = deptService.findById(id)
                .orElseThrow(() -> NotFoundException.of("dept", id));

        return DeptResponse.from(dept);
    }

    // 부서 등록
    @PostMapping
    public DeptResponse save(@Valid @RequestBody DeptRequest request) {
        Dept dept = deptService.save(request.toEntity());
        return DeptResponse.from(dept);
    }

    // 부서 수정
    @PutMapping("/{id}")
    public DeptResponse update(@PathVariable Long id, @Valid @RequestBody DeptRequest request) {
        Dept dept = deptService.update(id, request);
        return DeptResponse.from(dept);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        deptService.deleteById(id);
    }
}