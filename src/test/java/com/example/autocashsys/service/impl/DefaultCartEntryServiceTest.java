package com.example.autocashsys.service.impl;

import com.example.autocashsys.entity.CartEntry;
import com.example.autocashsys.repository.CartEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DefaultCartEntryServiceTest {

    @Mock
    private CartEntryRepository cartEntryRepository;

    @InjectMocks
    private DefaultCartEntryService cartEntryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCartEntryTest() {
        CartEntry cartEntry = new CartEntry();
        cartEntryService.createCartEntry(cartEntry);
        Mockito.verify(cartEntryRepository, Mockito.times(1)).save(cartEntry);
    }
}