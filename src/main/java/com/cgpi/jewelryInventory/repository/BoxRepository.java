package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxRepository extends JpaRepository<Box, Long> {
    boolean existsByIdentity(String identity);
}
