package com.example.trs_lab4_1.repository;


import com.example.trs_lab4_1.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    @Query("""
            select distinct p from Parameter p inner join p.parameterGroup.productGroupParameterGroups productGroupParameterGroups
            where productGroupParameterGroups.productGroup.id = ?1""")
    List<Parameter> findDistinctByParameterGroup_ProductGroupParameterGroups_ProductGroup_Id(Long productGroupId);


}