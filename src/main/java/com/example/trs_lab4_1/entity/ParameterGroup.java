package com.example.trs_lab4_1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "parameter_group")
public class ParameterGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "parameterGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductGroupParameterGroup> productGroupParameterGroups = new LinkedHashSet<>();

}