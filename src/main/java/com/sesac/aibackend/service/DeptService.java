package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.Dept;
import com.sesac.aibackend.dto.DeptRequest;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;

    @Transactional(readOnly = true)
    public List<Dept> findAll() {
        return deptRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Dept> findById(Long id) {
        return deptRepository.findById(id);
    }

    @Transactional
    public Dept save(Dept dept) {
        if (deptRepository.existsByDeptName(dept.getDeptName())) {
            throw new IllegalArgumentException("이미 존재하는 부서명입니다.");
        }

        return deptRepository.save(dept);
    }

    @Transactional
    public Dept update(Long id, DeptRequest request) {
        Dept dept = deptRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("dept", id));

        dept.setDeptName(request.deptName());
        dept.setPersonCount(request.personCount());

        return deptRepository.save(dept);
    }

    @Transactional
    public void deleteById(Long id) {
        deptRepository.deleteById(id);
    }
}