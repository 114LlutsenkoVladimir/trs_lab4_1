package com.example.trs_lab4_1.repository;

import com.example.trs_lab4_1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
    select p from Product p
        where not exists (
            select 1
                from ProductParameterValue ppv
                    where ppv.product = p
                        and ppv.parameter.id = :parameterId
        )
    """)
    List<Product> findProductsWithoutParameter(@Param("parameterId") Long parameterId);

    List<Product> findByProductGroup_Id(Long id);

    @Transactional
    default List<Product> deleteProductsByParameterIds(List<Long> parameterIds) {
        List<Product> products = findByProductParameterValues_Parameter_Id(parameterIds);
        deleteAll(products);
        return products;
    };

    @Query("""
            select distinct p from Product p inner join p.productParameterValues productParameterValues
            where productParameterValues.parameter.id in :parameterIds""")
    List<Product> findByProductParameterValues_Parameter_Id(@Param("parameterIds") List<Long> parameterIds);


}