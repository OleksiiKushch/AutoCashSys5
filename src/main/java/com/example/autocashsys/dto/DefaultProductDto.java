package com.example.autocashsys.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DefaultProductDto {

    private Integer id;
    private String imageSrc;
    private String name;
    private String manufacturer;
    private BigDecimal price;
    private String barcode;
    private Boolean isRequiredVerification;
}
