package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Dept;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record DeptRequest(
        @NotBlank String deptName,
        @Min(1)
        @Max(999)
        int personCount
) {
    public Dept toEntity() {
        return Dept.builder()
                .deptName(deptName)
                .personCount(personCount)
                .build();
    }
}