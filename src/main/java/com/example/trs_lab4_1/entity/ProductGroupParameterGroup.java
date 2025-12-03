package com.example.trs_lab4_1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "product_group_parameter_group")
public class ProductGroupParameterGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_group_id", nullable = false)
    private ProductGroup productGroup;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parameter_group_id", nullable = false)
    private ParameterGroup parameterGroup;

    public ProductGroupParameterGroup(ProductGroup productGroup,
                                      ParameterGroup parameterGroup) {
        this.productGroup = productGroup;
        this.parameterGroup = parameterGroup;
    }
}