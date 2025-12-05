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

	private final PieceRepository pieceRepo;
	private final BoxRepository boxRepo;

	public PieceService(PieceRepository pieceRepo, BoxRepository boxRepo) {
		this.pieceRepo = pieceRepo;
		this.boxRepo = boxRepo;
	}

	public Piece addPiece(Piece piece) {

		boxRepo.findById(piece.getBoxId()).orElseThrow(() -> new RuntimeException("Box not found"));

		piece.setCreatedAt(LocalDateTime.now());
		piece.setSold(false);
		piece.setUpdatedAt(null);

		return pieceRepo.save(piece);
	}

	public Piece updatePiece(Long id, Piece newPiece) {
		Piece p = getById(id);

		p.setBarcode(newPiece.getBarcode()); 
		p.setType(newPiece.getType());
		p.setPurity(newPiece.getPurity());
		p.setNetWeight(newPiece.getNetWeight());
		p.setVariableWeight(newPiece.getVariableWeight());
		p.setUpdatedAt(LocalDateTime.now());

		return pieceRepo.save(p);
	}

	public List<Piece> getAll() {
		return pieceRepo.findAll();
	}

	public Piece getById(Long id) {
		return pieceRepo.findById(id).orElseThrow(() -> new RuntimeException("Piece not found"));
	}

	@Transactional
	public Piece transferPiece(Long pieceId, Long newBoxId) {
		Piece piece = getById(pieceId);

		boxRepo.findById(newBoxId).orElseThrow(() -> new RuntimeException("Target box not found"));

		piece.setBoxId(newBoxId);
		piece.setUpdatedAt(LocalDateTime.now());

		return pieceRepo.save(piece);
	}

	@Transactional
	public Piece markSold(Long id) {
		Piece piece = getById(id);

		if (piece.isSold()) {
			throw new RuntimeException("Piece already sold");
		}

		piece.setSold(true);
		piece.setUpdatedAt(LocalDateTime.now());

		return pieceRepo.save(piece);
	}
}
