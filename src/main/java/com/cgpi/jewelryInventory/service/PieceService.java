package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.Piece;
import com.cgpi.jewelryInventory.repository.BoxRepository;
import com.cgpi.jewelryInventory.repository.PieceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PieceService {

	private final PieceRepository pieceRepository;
	private final BoxRepository boxRepository;
	private final BoxService boxService;
	private final InventoryStatementService statementService;

	public PieceService(
			PieceRepository pieceRepository,
			BoxRepository boxRepository,
			BoxService boxService,
			InventoryStatementService statementService) {

		this.pieceRepository = pieceRepository;
		this.boxRepository = boxRepository;
		this.boxService = boxService;
		this.statementService = statementService;
	}

	@Transactional
	public Piece addPiece(Piece piece) {
		boxRepository.findById(piece.getBoxId())
				.orElseThrow(() -> new RuntimeException("Box not found"));

		piece.setCreatedAt(LocalDateTime.now());
		piece.setUpdatedAt(null);
		piece.setSold(false);
		piece.setSoldAt(null);

		Piece saved = pieceRepository.save(piece);
		boxService.recalcBoxTotals(piece.getBoxId());

		statementService.log(
				"PIECE",
				saved.getId(),
				"ADD",
				saved.getBoxId(),
				null,
				null,
				saved.getNetWeight(),
				saved.getVariableWeight(),
				1,
				"Piece added"
		);

		return saved;
	}

	@Transactional
	public Piece updatePiece(Long id, Piece newPiece) {
		Piece existing = getById(id);
		Long boxId = existing.getBoxId();

		double oldNet = existing.getNetWeight();
		double oldVar = existing.getVariableWeight();

		existing.setBarcode(newPiece.getBarcode());
		existing.setType(newPiece.getType());
		existing.setPurity(newPiece.getPurity());
		existing.setNetWeight(newPiece.getNetWeight());
		existing.setVariableWeight(newPiece.getVariableWeight());
		existing.setUpdatedAt(LocalDateTime.now());

		Piece saved = pieceRepository.save(existing);
		boxService.recalcBoxTotals(boxId);

		statementService.log(
				"PIECE",
				saved.getId(),
				"UPDATE",
				boxId,
				null,
				null,
				saved.getNetWeight() - oldNet,
				saved.getVariableWeight() - oldVar,
				0,
				"Piece updated"
		);

		return saved;
	}

	public List<Piece> getAll() {
		return pieceRepository.findAll();
	}

	public Piece getById(Long id) {
		return pieceRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Piece not found"));
	}

	public List<Piece> getByBoxId(Long boxId) {
		return pieceRepository.findByBoxId(boxId);
	}

	@Transactional
	public Piece transferPiece(Long pieceId, Long newBoxId) {
		Piece piece = getById(pieceId);

		boxRepository.findById(newBoxId)
				.orElseThrow(() -> new RuntimeException("Target box not found"));

		Long oldBoxId = piece.getBoxId();

		piece.setBoxId(newBoxId);
		piece.setUpdatedAt(LocalDateTime.now());

		Piece saved = pieceRepository.save(piece);

		boxService.recalcBoxTotals(oldBoxId);
		boxService.recalcBoxTotals(newBoxId);

		statementService.log(
				"PIECE",
				saved.getId(),
				"TRANSFER",
				null,
				oldBoxId,
				newBoxId,
				0.0,
				0.0,
				0,
				"Piece transferred"
		);

		return saved;
	}

	@Transactional
	public Piece markSold(Long id) {
		Piece piece = getById(id);

		if (piece.isSold()) {
			throw new RuntimeException("Piece already sold");
		}

		piece.setSold(true);
		piece.setSoldAt(LocalDateTime.now());
		piece.setUpdatedAt(LocalDateTime.now());

		Piece saved = pieceRepository.save(piece);
		boxService.recalcBoxTotals(piece.getBoxId());

		statementService.log(
				"PIECE",
				saved.getId(),
				"SELL",
				saved.getBoxId(),
				null,
				null,
				-saved.getNetWeight(),
				-saved.getVariableWeight(),
				-1,
				"Piece sold"
		);

		return saved;
	}

	@Transactional
	public void deletePiece(Long id) {
		Piece piece = getById(id);
		Long boxId = piece.getBoxId();
		boolean wasSold = piece.isSold();

		pieceRepository.delete(piece);

		if (!wasSold) {
			boxService.recalcBoxTotals(boxId);

			statementService.log(
					"PIECE",
					id,
					"DELETE",
					boxId,
					null,
					null,
					-piece.getNetWeight(),
					-piece.getVariableWeight(),
					-1,
					"Piece deleted"
			);
		}
	}
}
