package com.example.trs_lab4_1.service;


import com.example.trs_lab4_1.entity.ParameterGroup;
import com.example.trs_lab4_1.repository.ParameterGroupRepository;
import org.springframework.stereotype.Service;

@Service
public class ParameterGroupService extends AbstractCrudService<ParameterGroup, Long, ParameterGroupRepository> {
    public ParameterGroupService(ParameterGroupRepository repository) {
        super(repository);
    }
}
