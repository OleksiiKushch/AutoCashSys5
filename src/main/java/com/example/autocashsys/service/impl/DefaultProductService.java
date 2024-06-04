package com.example.autocashsys.service.impl;

import com.example.autocashsys.entity.Product;
import com.example.autocashsys.dto.ProductSearchParameter;
import com.example.autocashsys.repository.ProductRepository;
import com.example.autocashsys.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> getForBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    @Override
    public List<Product> getProductsByCriteria(String searchParameter, String searchPattern) {
        var parameter = ProductSearchParameter.valueOf(searchParameter);
        return switch (parameter) {
            case NAME -> productRepository.findByNameContains(searchPattern);
            case BARCODE -> productRepository.findByBarcodeContains(searchPattern);
        };
    }
}
