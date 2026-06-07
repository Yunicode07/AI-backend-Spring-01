package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<Dept, Long> {

    boolean existsByDeptName(String deptName);
}