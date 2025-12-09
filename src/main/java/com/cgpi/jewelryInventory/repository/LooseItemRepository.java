package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.LooseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LooseItemRepository extends JpaRepository<LooseItem, Long> {
    List<LooseItem> findByBoxId(Long boxId);
}
