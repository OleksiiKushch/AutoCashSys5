package com.example.autocashsys.service.impl;

import com.example.autocashsys.entity.Receipt;
import com.example.autocashsys.repository.ReceiptRepository;
import com.example.autocashsys.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultReceiptService implements ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Override
    public void createReceipt(Receipt receipt) {
        receiptRepository.save(receipt);
    }
}
