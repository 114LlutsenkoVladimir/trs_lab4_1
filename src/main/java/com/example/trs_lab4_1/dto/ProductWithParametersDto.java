package com.example.trs_lab4_1.dto;

import lombok.Getter;

import java.time.LocalDate;


@Getter
public class ProductWithParametersDto {

        ProductMainFields product;

        ParameterWithValue parameter;

    public ProductWithParametersDto(Long productId, String productName,
                                    String productDescription, LocalDate productReleaseDate,
                                    Long productGroupId, String productGroupName,

                                    Long parameterId, String parameterName,
                                    String parameterUnit, String parameterValue) {

        product = new ProductMainFields(productId, productName, productDescription,
                productReleaseDate, productGroupId, productGroupName);

        parameter = new ParameterWithValue(parameterId, parameterName,
                parameterUnit, parameterValue);
    }


}
