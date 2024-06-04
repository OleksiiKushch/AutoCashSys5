package com.example.autocashsys.service;

import com.example.autocashsys.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getForBarcode(String barcode);
    List<Product> getProductsByCriteria(String searchParameter, String searchPattern);
}
