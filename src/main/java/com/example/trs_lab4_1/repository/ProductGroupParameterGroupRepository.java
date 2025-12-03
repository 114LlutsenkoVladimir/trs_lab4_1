package com.example.trs_lab4_1.repository;


import com.example.trs_lab4_1.entity.Parameter;
import com.example.trs_lab4_1.entity.ProductGroupParameterGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductGroupParameterGroupRepository extends JpaRepository<ProductGroupParameterGroup, Long> {
    @Query("""
        select distinct p
        from Parameter p
            join p.parameterGroup pg
            join pg.productGroupParameterGroups link
                on link.parameterGroup = p.parameterGroup
            where link.productGroup.id = :productGroupId
    """)
    List<Parameter> getParametersForProductGroup(Long productGroupId);


    Optional<ProductGroupParameterGroup> findByProductGroup_IdAndParameterGroup_Id(Long productGroupId,
                                                                                   Long parameterGroupId);

}