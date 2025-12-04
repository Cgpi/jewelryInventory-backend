package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.Purity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurityRepository extends JpaRepository<Purity, Long> {
    List<Purity> findByDeletedFalse();
}
