package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.Dept;
import com.sesac.aibackend.domain.Staff;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.DeptRepository;
import com.sesac.aibackend.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final DeptRepository deptRepository;

    @Transactional
    public Staff save(Long deptId, int empNo, String name, String role, LocalDate hireDate) {
        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(() -> NotFoundException.of("dept", deptId));

        return staffRepository.save(
                Staff.builder()
                        .dept(dept)
                        .empNo(empNo)
                        .name(name)
                        .role(role)
                        .hireDate(hireDate)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public List<Staff> findByDeptId(Long deptId) {
        deptRepository.findById(deptId)
                .orElseThrow(() -> NotFoundException.of("dept", deptId));

        return staffRepository.findByDeptIdOrderByHireDateDesc(deptId);
    }

    @Transactional(readOnly = true)
    public List<Staff> findByDeptIdWithDept(Long deptId) {
        deptRepository.findById(deptId)
                .orElseThrow(() -> NotFoundException.of("dept", deptId));

        return staffRepository.findByDeptIdWithDept(deptId);
    }

    @Transactional
    public void deleteById(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> NotFoundException.of("staff", staffId));

        staffRepository.delete(staff);
    }
}