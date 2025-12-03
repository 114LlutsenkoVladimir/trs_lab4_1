package com.example.trs_lab4_1.service;

import com.example.trs_lab4_1.entity.Product;
import com.example.trs_lab4_1.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService extends AbstractCrudService<Product, Long, ProductRepository> {

    public ProductService(ProductRepository repository) {
        super(repository);
    }

    public List<Product> findProductsWithoutParameter(Long parameterId) {
        return repository.findProductsWithoutParameter(parameterId);
    }

    public List<Product> findProductsByProductGroup(Long productGroupId) {
        return repository.findByProductGroup_Id(productGroupId);
    }

    public List<Product> deleteProductsByParameterIds(List<Long> parameterIds) {
        return repository.deleteProductsByParameterIds(parameterIds);
    }

    public List<Product> deleteProductsByParameterIds(Long parameterId) {
        return deleteProductsByParameterIds
                (new ArrayList<>(List.of(parameterId)));
    }

}
