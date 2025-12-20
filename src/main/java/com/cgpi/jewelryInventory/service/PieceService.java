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

	public PieceService(PieceRepository pieceRepository, BoxRepository boxRepository, BoxService boxService,
			InventoryStatementService statementService) {

		this.pieceRepository = pieceRepository;
		this.boxRepository = boxRepository;
		this.boxService = boxService;
		this.statementService = statementService;
	}

	@Transactional
	public Piece addPiece(Piece piece) {

		boxRepository.findById(piece.getBoxId()).orElseThrow(() -> new RuntimeException("Box not found"));

		piece.setCreatedAt(LocalDateTime.now());
		piece.setUpdatedAt(null);
		piece.setSold(false);
		piece.setSoldAt(null);

		Piece saved = pieceRepository.save(piece);
		boxService.recalcBoxTotals(saved.getBoxId());

		statementService.logPiece(saved, "ADD", null, null, saved.getNetWeight(), saved.getVariableWeight(), 1,
				"Piece added");

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

		statementService.logPiece(saved, "UPDATE", null, null, saved.getNetWeight() - oldNet,
				saved.getVariableWeight() - oldVar, 0, "Piece updated");

		return saved;
	}

	public List<Piece> getAll() {
		return pieceRepository.findAll();
	}

	public Piece getById(Long id) {
		return pieceRepository.findById(id).orElseThrow(() -> new RuntimeException("Piece not found"));
	}

	public List<Piece> getByBoxId(Long boxId) {
		return pieceRepository.findByBoxId(boxId);
	}

	@Transactional
	public Piece transferPiece(Long pieceId, Long newBoxId) {

		Piece piece = getById(pieceId);

		boxRepository.findById(newBoxId).orElseThrow(() -> new RuntimeException("Target box not found"));

		Long oldBoxId = piece.getBoxId();
		double net = piece.getNetWeight();
		double var = piece.getVariableWeight();

		piece.setBoxId(newBoxId);
		piece.setUpdatedAt(LocalDateTime.now());

		Piece saved = pieceRepository.save(piece);

		statementService.logPiece(saved, "TRANSFER_OUT", oldBoxId, newBoxId, -net, -var, -1, "Piece transferred out");

		statementService.logPiece(saved, "TRANSFER_IN", oldBoxId, newBoxId, net, var, 1, "Piece transferred in");

		boxService.recalcBoxTotals(oldBoxId);
		boxService.recalcBoxTotals(newBoxId);

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
		boxService.recalcBoxTotals(saved.getBoxId());

		statementService.logPiece(saved, "SELL", null, null, -saved.getNetWeight(), -saved.getVariableWeight(), -1,
				"Piece sold");

		return saved;
	}

	@Transactional
	public void deletePiece(Long id) {

		Piece piece = getById(id);
		Long boxId = piece.getBoxId();

		pieceRepository.delete(piece);
		boxService.recalcBoxTotals(boxId);

		statementService.logPiece(piece, "DELETE", null, null, -piece.getNetWeight(), -piece.getVariableWeight(), -1,
				"Piece deleted");
	}

	@Transactional
	public int deleteAllSoldPieces() {

		List<Piece> soldPieces = pieceRepository.findBySoldTrue();

		if (soldPieces.isEmpty()) {
			return 0;
		}

		for (Piece piece : soldPieces) {
			Long boxId = piece.getBoxId();

			pieceRepository.delete(piece);
			boxService.recalcBoxTotals(boxId);
		}

		return soldPieces.size();
	}

	@Transactional
	public int deleteSoldPiecesByBox(Long boxId) {

		List<Piece> soldPieces = pieceRepository.findByBoxIdAndSoldTrue(boxId);

		if (soldPieces.isEmpty()) {
			return 0;
		}

		for (Piece piece : soldPieces) {

			pieceRepository.delete(piece);
		}

		boxService.recalcBoxTotals(boxId);
		return soldPieces.size();
	}

	@Transactional
	public int deleteSoldPiecesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {

		List<Piece> soldPieces = pieceRepository.findBySoldTrueAndSoldAtBetween(startDate, endDate);

		if (soldPieces.isEmpty()) {
			return 0;
		}

		for (Piece piece : soldPieces) {
			Long boxId = piece.getBoxId();

			pieceRepository.delete(piece);
			boxService.recalcBoxTotals(boxId);
		}

		return soldPieces.size();
	}

}
