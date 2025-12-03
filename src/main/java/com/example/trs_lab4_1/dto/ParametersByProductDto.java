package com.example.trs_lab4_1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ParametersByProductDto {

    private ProductMainFields product;

    private List<ParameterWithValue> parameters = new ArrayList<>();

    public ParametersByProductDto(Long productId, String productName, String productDescription,
                                  LocalDate productReleaseDate, Long productGroupId, String productGroupName) {
        this.product = new ProductMainFields(productId, productName, productDescription,
                productReleaseDate, productGroupId, productGroupName);
    }

    public ParametersByProductDto(ProductMainFields product) {
        this.product = product;
    }

    public void addParameterWithValue(ParameterWithValue parameterWithValue) {
        parameters.add(parameterWithValue);
    }
}
