package com.example.autocashsys.repository;

import com.example.autocashsys.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByBarcode(String barcode);
    List<Product> findByNameContains(String name);
    List<Product> findByBarcodeContains(String name);
}
