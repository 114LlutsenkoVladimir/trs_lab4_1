package com.example.trs_lab4_1.service;

import com.example.trs_lab4_1.entity.Parameter;
import com.example.trs_lab4_1.repository.ParameterRepository;
import com.example.trs_lab4_1.repository.ProductGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParameterService extends AbstractCrudService<Parameter, Long, ParameterRepository> {

    private final ProductGroupRepository productGroupRepository;

    public ParameterService(ParameterRepository repository,
                            ProductGroupRepository productGroupRepository) {
        super(repository);
        this.productGroupRepository = productGroupRepository;
    }

    public List<Parameter> getParametersByProductGroup(Long productGroupId) {
        productGroupRepository.findById(productGroupId);
        return repository.findDistinctByParameterGroup_ProductGroupParameterGroups_ProductGroup_Id(productGroupId);
    }
}
