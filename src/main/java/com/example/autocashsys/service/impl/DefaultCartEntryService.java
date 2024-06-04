package com.example.autocashsys.service.impl;

import com.example.autocashsys.entity.CartEntry;
import com.example.autocashsys.repository.CartEntryRepository;
import com.example.autocashsys.service.CartEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultCartEntryService implements CartEntryService {

    private final CartEntryRepository cartEntryRepository;

    @Override
    public void createCartEntry(CartEntry cartEntry) {
        cartEntryRepository.save(cartEntry);
    }
}
