package com.example.trs_lab4_1.dto;

public record ParameterWithValue(
        Long parameterId,
        String parameterName,
        String parameterUnit,
        String parameterValue
) {}
