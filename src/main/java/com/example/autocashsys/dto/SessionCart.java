package com.example.autocashsys.dto;

import com.example.autocashsys.entity.Employee;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class SessionCart {

    private Map<DefaultProductDto, EntryInfo> products;
    private BigDecimal totalSum;
    private Boolean isRequiredVerification;
    private Employee approver;

    public SessionCart() {
        products = new LinkedHashMap<>();
        totalSum = BigDecimal.ZERO;
        isRequiredVerification = false;
    }

    public void put(DefaultProductDto product, Integer amount) {
        if (product.getIsRequiredVerification()) {
            isRequiredVerification = true;
        }
        EntryInfo entryInfo = products.get(product);
        if (entryInfo == null) {
            entryInfo = new EntryInfo(amount, product.getPrice().multiply(BigDecimal.valueOf(amount)));
            this.products.put(product, entryInfo);
        } else {
            int newAmount = entryInfo.getAmount() + amount;
            BigDecimal newSum = product.getPrice().multiply(BigDecimal.valueOf(newAmount));
            entryInfo.setAmount(newAmount);
            entryInfo.setSum(newSum);
        }
        totalSum = totalSum.add(product.getPrice().multiply(BigDecimal.valueOf(amount)));
    }

    public void replace(DefaultProductDto product, Integer amount) {
        EntryInfo entryInfo = products.get(product);
        if (entryInfo != null) {
            totalSum = totalSum.subtract(entryInfo.getSum());

            BigDecimal newSum = product.getPrice().multiply(BigDecimal.valueOf(amount));
            entryInfo.setAmount(amount);
            entryInfo.setSum(newSum);

            totalSum = totalSum.add(newSum);
        }
    }

    public void delete(DefaultProductDto product) {
        EntryInfo entryInfo = products.get(product);
        if (entryInfo != null) {
            totalSum = totalSum.subtract(entryInfo.getSum());

            products.remove(product);

            if (product.getIsRequiredVerification()) {
                recalculateVerificationRequirements();
            }
        }
    }

    private void recalculateVerificationRequirements() {
        isRequiredVerification = products.keySet().stream()
                .anyMatch(DefaultProductDto::getIsRequiredVerification);
    }
}
