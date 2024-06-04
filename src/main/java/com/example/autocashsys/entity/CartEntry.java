package com.example.autocashsys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Data
@Entity(name = "cart_entries")
public class CartEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Receipt receipt;

    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
        receipt.getCartEntries().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        product.getCartEntries().add(this);
    }
}
