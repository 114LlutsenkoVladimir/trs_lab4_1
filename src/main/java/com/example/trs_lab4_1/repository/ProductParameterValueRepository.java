package com.example.trs_lab4_1.repository;


import com.example.trs_lab4_1.dto.ProductWithParametersDto;
import com.example.trs_lab4_1.entity.ProductParameterValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductParameterValueRepository extends JpaRepository<ProductParameterValue, Long> {
    @Query("select p from ProductParameterValue p where p.product.id = ?1")
    List<ProductParameterValue> findByProduct_Id(Long id);

    @Query("""
        select new com.example.trs_lab4_1.dto.ProductWithParametersDto(
            p.id, p.name, p.description, p.releaseDate,
                pg.id, pg.name,
                    pm.id, pm.name, pm.unit, ppv.parameterValue
        )
        from ProductParameterValue ppv
        join ppv.product p
        join ppv.parameter pm
        join p.productGroup pg
        where p.id in :productIds
    """)
    List<ProductWithParametersDto> findProductWithParametersByProductId(@Param("productIds") List<Long> productIds);



}