package com.example.autocashsys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imageSrc;

    @Column(nullable = false)
    private String name;

    private String manufacturer;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(nullable = false)
    private Boolean isRequiredVerification;

    @OneToMany(mappedBy = "product")
    private List<CartEntry> cartEntries = new ArrayList<>();
}
