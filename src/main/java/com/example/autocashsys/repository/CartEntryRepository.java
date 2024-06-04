package com.example.autocashsys.repository;

import com.example.autocashsys.entity.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartEntryRepository extends JpaRepository<CartEntry, Integer> {
}
