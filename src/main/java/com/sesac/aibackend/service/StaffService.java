package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.Staff;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.DeptRepository;
import com.sesac.aibackend.repository.StaffRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final DeptRepository deptRepository;

}
