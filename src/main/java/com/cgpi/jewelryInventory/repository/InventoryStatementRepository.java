package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.InventoryStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryStatementRepository extends JpaRepository<InventoryStatement, Long> {

	List<InventoryStatement> findByBoxId(Long boxId);

	List<InventoryStatement> findByEntityTypeAndEntityId(String entityType, Long entityId);
}
