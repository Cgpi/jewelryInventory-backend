package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.InventoryStatement;
import com.cgpi.jewelryInventory.repository.InventoryStatementRepository;
import com.cgpi.jewelryInventory.security.SecurityUtil;
import org.springframework.stereotype.Service;

@Service
public class InventoryStatementService {

	private final InventoryStatementRepository repo;

	public InventoryStatementService(InventoryStatementRepository repo) {
		this.repo = repo;
	}

	public void log(
			String entityType,
			Long entityId,
			String action,
			Long boxId,
			Long fromBoxId,
			Long toBoxId,
			Double netWeightChange,
			Double variableWeightChange,
			Integer pieceCountChange,
			String remark
	) {
		InventoryStatement s = new InventoryStatement();

		s.setEntityType(entityType);
		s.setEntityId(entityId);
		s.setAction(action);
		s.setBoxId(boxId);
		s.setFromBoxId(fromBoxId);
		s.setToBoxId(toBoxId);
		s.setNetWeightChange(netWeightChange);
		s.setVariableWeightChange(variableWeightChange);
		s.setPieceCountChange(pieceCountChange);
		s.setPerformedByRole(SecurityUtil.getRole());
		s.setRemark(remark);

		repo.save(s);
	}
}
