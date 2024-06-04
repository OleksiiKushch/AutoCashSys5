package com.example.autocashsys.service.impl;

import com.example.autocashsys.entity.Receipt;
import com.example.autocashsys.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DefaultReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private DefaultReceiptService receiptService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createReceiptTest() {
        Receipt receipt = new Receipt();
        receiptService.createReceipt(receipt);

        verify(receiptRepository, times(1)).save(receipt);
    }
}