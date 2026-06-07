package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Staff;

import java.time.LocalDate;

public record StaffResponse(
        Long id,
        int empNo,
        String name,
        Long deptId,
        String deptName,
        String role,
        LocalDate hireDate
) {

    public static StaffResponse from(Staff staff) {
        return new StaffResponse(
                staff.getId(),
                staff.getEmpNo(),
                staff.getName(),
                staff.getDept().getId(),
                null,
                staff.getRole(),
                staff.getHireDate()
        );
    }

    public static StaffResponse fromWithDept(Staff staff) {
        return new StaffResponse(
                staff.getId(),
                staff.getEmpNo(),
                staff.getName(),
                staff.getDept().getId(),
                staff.getDept().getDeptName(),
                staff.getRole(),
                staff.getHireDate()
        );
    }
}