package com.sesac.aibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record StaffRequest(
        @NotNull Long deptId,
        @Positive int empNo,
        @NotBlank String name,
        @NotBlank String role,
        @NotNull LocalDate hireDate
) {
}