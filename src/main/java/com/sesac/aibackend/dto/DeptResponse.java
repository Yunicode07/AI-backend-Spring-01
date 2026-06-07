package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Dept;

/**
 * 부서 응답 DTO.
 */
public record DeptResponse(
        Long id,
        String deptName,
        int personCount
) {

    // Entity 객체를 DTO 객체로 변환
    public static DeptResponse from(Dept dept) {
        return new DeptResponse(
                dept.getId(),
                dept.getDeptName(),
                dept.getPersonCount()
        );
    }
}