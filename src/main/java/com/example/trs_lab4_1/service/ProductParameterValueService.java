package com.example.trs_lab4_1.service;

import com.example.trs_lab4_1.dto.ParametersByProductDto;
import com.example.trs_lab4_1.dto.ProductWithParametersDto;
import com.example.trs_lab4_1.entity.ProductParameterValue;
import com.example.trs_lab4_1.repository.ProductParameterValueRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductParameterValueService extends AbstractCrudService<ProductParameterValue, Long,
        ProductParameterValueRepository> {

    public ProductParameterValueService(ProductParameterValueRepository repository) {
        super(repository);
    }

    public List<ParametersByProductDto> getParametersByProduct(List<Long> productIds) {
        List<ProductWithParametersDto> productWithParametersDtos =
                repository.findProductWithParametersByProductId(productIds);

        Map<Long, ParametersByProductDto> map = new LinkedHashMap<>();

        for (ProductWithParametersDto dto : productWithParametersDtos) {
            Long productId = dto.getProduct().productId();

            ParametersByProductDto resultDto =
                    map.computeIfAbsent(productId,
                            id -> new ParametersByProductDto(dto.getProduct())
                    );
            resultDto.addParameterWithValue(dto.getParameter());
        }
        return new ArrayList<>(map.values());
    }

    public List<ParametersByProductDto> getParametersByProduct(Long productId) {
        return getParametersByProduct(new ArrayList<>(List.of(productId)));
    }


}
