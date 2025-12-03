package com.example.trs_lab4_1.dto;

import java.time.LocalDate;

public record ProductMainFields(
        Long productId,
        String productName,
        String productDescription,
        LocalDate productReleaseDate,
        Long productGroupId,
        String productGroupName
)
{}
