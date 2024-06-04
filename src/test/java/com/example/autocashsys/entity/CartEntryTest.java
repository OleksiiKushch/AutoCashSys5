package com.example.autocashsys.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CartEntryTest {

    private static final Integer PRODUCT_AMOUNT = 2;
    private static final BigDecimal PRODUCT_UNIT_PRICE = BigDecimal.valueOf(100.0);

    private CartEntry cartEntry;
    private Receipt receipt;
    private Product product;

    @BeforeEach
    public void setUp() {
        cartEntry = new CartEntry();
        receipt = Mockito.mock(Receipt.class);
        product = Mockito.mock(Product.class);

        // Mocking the behavior of getCartEntries to return an empty array list on first call and then collect added cartEntries
        Mockito.when(receipt.getCartEntries()).thenReturn(new ArrayList<>());
        Mockito.when(product.getCartEntries()).thenReturn(new ArrayList<>());

        cartEntry.setProduct(product);
        cartEntry.setReceipt(receipt);
        cartEntry.setAmount(PRODUCT_AMOUNT);
        cartEntry.setUnitPrice(PRODUCT_UNIT_PRICE);
    }

    @Test
    public void testReceiptAndProductAssociations() {
        assertAll(
                () -> assertTrue(receipt.getCartEntries().contains(cartEntry), "Receipt should contain the cart entry"),
                () -> assertTrue(product.getCartEntries().contains(cartEntry), "Product should contain the cart entry")
        );
    }

    @Test
    public void testCartEntryValues() {
        assertAll(
                () -> assertEquals(PRODUCT_AMOUNT, cartEntry.getAmount()),
                () -> assertEquals(PRODUCT_UNIT_PRICE, cartEntry.getUnitPrice())
        );
    }
}