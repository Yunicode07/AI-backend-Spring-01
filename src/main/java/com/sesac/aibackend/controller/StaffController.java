package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Staff;
import com.sesac.aibackend.dto.StaffRequest;
import com.sesac.aibackend.dto.StaffResponse;
import com.sesac.aibackend.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public List<StaffResponse> findByDeptId(@RequestParam Long deptId) {
        return staffService.findByDeptId(deptId)
                .stream()
                .map(StaffResponse::from)
                .toList();
    }

    @GetMapping("/with-dept")
    public List<StaffResponse> findByDeptIdWithDept(@RequestParam Long deptId) {
        return staffService.findByDeptIdWithDept(deptId)
                .stream()
                .map(StaffResponse::fromWithDept)
                .toList();
    }

    @PostMapping
    public StaffResponse save(@Valid @RequestBody StaffRequest request) {
        Staff staff = staffService.save(
                request.deptId(),
                request.empNo(),
                request.name(),
                request.role(),
                request.hireDate()
        );

        return StaffResponse.from(staff);
    }

    @DeleteMapping("/{staffId}")
    public void deleteById(@PathVariable Long staffId) {
        staffService.deleteById(staffId);
    }
}