package com.example.autocashsys.service.impl;

import com.example.autocashsys.entity.Product;
import com.example.autocashsys.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DefaultProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DefaultProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getForBarcodeTest() {
        String barcode = "barcode";
        Product product = new Product();
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getForBarcode(barcode);

        assertEquals(Optional.of(product), result);
    }

    @Test
    public void getProductsByCriteriaTest_name() {
        String searchParameter = "NAME";
        String searchPattern = "Pattern";
        List<Product> products = Collections.singletonList(new Product());
        when(productRepository.findByNameContains(searchPattern)).thenReturn(products);

        List<Product> result = productService.getProductsByCriteria(searchParameter, searchPattern);

        assertEquals(products, result);
    }

    @Test
    public void getProductsByCriteriaTest_barcode() {
        String searchParameter = "BARCODE";
        String searchPattern = "Pattern";
        List<Product> products = Collections.singletonList(new Product());
        when(productRepository.findByBarcodeContains(searchPattern)).thenReturn(products);

        List<Product> result = productService.getProductsByCriteria(searchParameter, searchPattern);

        assertEquals(products, result);
    }
}