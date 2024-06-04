package com.example.autocashsys.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class SessionCartTest {

    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(100.0);
    private static final Integer PRODUCT_AMOUNT = 2;
    private static final Integer EQUAL = 0;

    private SessionCart sessionCart;
    private DefaultProductDto product;

    @BeforeEach
    public void setUp() {
        sessionCart = new SessionCart();
        product = Mockito.mock(DefaultProductDto.class);
        Mockito.when(product.getPrice()).thenReturn(PRODUCT_PRICE);
        Mockito.when(product.getIsRequiredVerification()).thenReturn(true);
    }

    @Test
    public void putProductToCart() {
        sessionCart.put(product, PRODUCT_AMOUNT);
        assertAll(
                () -> assertEquals(PRODUCT_PRICE.multiply(BigDecimal.valueOf(PRODUCT_AMOUNT)), sessionCart.getTotalSum()),
                () -> assertTrue(sessionCart.getIsRequiredVerification()),
                () -> assertEquals(Collections.singletonMap(product, new EntryInfo(PRODUCT_AMOUNT, PRODUCT_PRICE.multiply(BigDecimal.valueOf(
                        PRODUCT_AMOUNT)))), sessionCart.getProducts())
        );
    }

    @Test
    public void replaceProductInCart() {
        sessionCart.put(product, PRODUCT_AMOUNT);
        sessionCart.replace(product, PRODUCT_AMOUNT);
        assertAll(
                () -> assertEquals(PRODUCT_PRICE.multiply(BigDecimal.valueOf(PRODUCT_AMOUNT)), sessionCart.getTotalSum()),
                () -> assertTrue(sessionCart.getIsRequiredVerification()),
                () -> assertEquals(Collections.singletonMap(product, new EntryInfo(PRODUCT_AMOUNT, PRODUCT_PRICE.multiply(BigDecimal.valueOf(
                        PRODUCT_AMOUNT)))), sessionCart.getProducts())
        );
    }

    @Test
    public void deleteProductFromCart() {
        sessionCart.put(product, PRODUCT_AMOUNT);
        sessionCart.delete(product);
        assertAll(
                () -> assertEquals(EQUAL, BigDecimal.ZERO.compareTo(sessionCart.getTotalSum())),
                () -> assertFalse(sessionCart.getIsRequiredVerification()),
                () -> assertTrue(sessionCart.getProducts().isEmpty())
        );
    }
}