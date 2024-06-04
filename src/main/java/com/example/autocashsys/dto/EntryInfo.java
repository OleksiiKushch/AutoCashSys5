package com.example.autocashsys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class EntryInfo {

    private Integer amount;
    private BigDecimal sum;
}
