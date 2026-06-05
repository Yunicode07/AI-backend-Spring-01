package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
