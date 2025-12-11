package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.InventoryStatement;
import com.cgpi.jewelryInventory.model.Piece;
import com.cgpi.jewelryInventory.model.LooseItem;
import com.cgpi.jewelryInventory.repository.InventoryStatementRepository;
import com.cgpi.jewelryInventory.security.SecurityUtil;
import org.springframework.stereotype.Service;

@Service
public class InventoryStatementService {

	private final InventoryStatementRepository repo;

	public InventoryStatementService(InventoryStatementRepository repo) {
		this.repo = repo;
	}

	public void logPiece(Piece piece, String action, Long fromBoxId, Long toBoxId, Double netChange, Double varChange,
			Integer countChange, String remark) {

		InventoryStatement s = new InventoryStatement();

		s.setEntityType("PIECE");
		s.setEntityId(piece.getId());
		s.setBarcode(piece.getBarcode());
		s.setPieceType(piece.getType());
		s.setPurity(piece.getPurity());

		s.setAction(action);
		s.setBoxId(piece.getBoxId());
		s.setFromBoxId(fromBoxId);
		s.setToBoxId(toBoxId);

		s.setNetWeightChange(netChange);
		s.setVariableWeightChange(varChange);
		s.setPieceCountChange(countChange);

		s.setPerformedByRole(SecurityUtil.getRole());
		s.setRemark(remark);

		repo.save(s);
	}

	public void logLoose(LooseItem item, String action, Long fromBoxId, Long toBoxId, Double netChange,
			Double varChange, String remark) {

		InventoryStatement s = new InventoryStatement();

		s.setEntityType("LOOSE_ITEM");
		s.setEntityId(item.getId());
		s.setLooseItemName(item.getName());

		s.setAction(action);
		s.setBoxId(item.getBoxId());
		s.setFromBoxId(fromBoxId);
		s.setToBoxId(toBoxId);

		s.setNetWeightChange(netChange);
		s.setVariableWeightChange(varChange);
		s.setPieceCountChange(0);

		s.setPerformedByRole(SecurityUtil.getRole());
		s.setRemark(remark);

		repo.save(s);
	}
}
