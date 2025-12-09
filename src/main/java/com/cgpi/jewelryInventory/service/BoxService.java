package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.Box;
import com.cgpi.jewelryInventory.model.LooseItem;
import com.cgpi.jewelryInventory.model.Piece;
import com.cgpi.jewelryInventory.repository.BoxRepository;
import com.cgpi.jewelryInventory.repository.CounterRepository;
import com.cgpi.jewelryInventory.repository.LooseItemRepository;
import com.cgpi.jewelryInventory.repository.PieceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoxService {

	private final BoxRepository repo;
    private final CounterRepository counterRepository;
    private final PieceRepository pieceRepository;
    private final LooseItemRepository looseItemRepository;

    public BoxService(BoxRepository repo, CounterRepository counterRepository,
                      PieceRepository pieceRepository, LooseItemRepository looseItemRepository) {
        this.repo = repo;
        this.counterRepository = counterRepository;
        this.pieceRepository = pieceRepository;
        this.looseItemRepository = looseItemRepository;
    }

	public Box createBox(Box box) {
		if (box.getCounterId() == null) {
			throw new RuntimeException("counterId is required");
		}

		counterRepository.findById(box.getCounterId()).orElseThrow(() -> new RuntimeException("Counter not found"));

		if (repo.existsByIdentity(box.getIdentity())) {
			throw new RuntimeException("Identity already exists");
		}

		if (box.getFixedWeight() == null) {
			throw new RuntimeException("fixedWeight is required");
		}

		box.setNetWeight(0.0);
		box.setVariableWeight(0.0);
		box.setTotalPiece(0);
		box.recalcGrossWeight();
		box.setCreatedAt(LocalDateTime.now());
		box.setUpdatedAt(null);

		return repo.save(box);
	}

	public Box updateBox(Long id, Box newBox) {
		Box box = getById(id);

		if (newBox.getIdentity() == null) {
			throw new RuntimeException("identity is required");
		}

		if (!box.getIdentity().equals(newBox.getIdentity()) && repo.existsByIdentity(newBox.getIdentity())) {
			throw new RuntimeException("Identity already exists");
		}

		box.setType(newBox.getType());
		box.setIdentity(newBox.getIdentity());
		if (newBox.getFixedWeight() != null) {
			box.setFixedWeight(newBox.getFixedWeight());
		}

		box.recalcGrossWeight();

		box.setUpdatedAt(LocalDateTime.now());
		return repo.save(box);
	}

	public List<Box> getAll() {
		return repo.findAll();
	}

	public Box getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Box not found"));
	}

	public List<Box> getByCounterId(Long counterId) {
		return repo.findByCounterId(counterId);
	}

	public Box transferBox(Long boxId, Long targetCounterId) {
		Box box = getById(boxId);

		if (box.getCounterId().equals(targetCounterId)) {
			throw new RuntimeException("Box is already in this counter");
		}

		counterRepository.findById(targetCounterId).orElseThrow(() -> new RuntimeException("Target counter not found"));

		box.setCounterId(targetCounterId);
		box.setUpdatedAt(LocalDateTime.now());

		return repo.save(box);
	}

    public void recalcBoxTotals(Long boxId) {
        Box box = repo.findById(boxId).orElseThrow(() -> new RuntimeException("Box not found"));

        // Pieces
        List<Piece> pieces = pieceRepository.findByBoxId(boxId);
        double totalPieceNet = pieces.stream().filter(p -> !p.isSold())
                .mapToDouble(p -> p.getNetWeight() == null ? 0.0 : p.getNetWeight()).sum();
        double totalPieceVar = pieces.stream().filter(p -> !p.isSold())
                .mapToDouble(p -> p.getVariableWeight() == null ? 0.0 : p.getVariableWeight()).sum();
        int totalPieceCount = (int) pieces.stream().filter(p -> !p.isSold()).count();

        // LooseItems
        List<LooseItem> looseItems = looseItemRepository.findByBoxId(boxId);
        double totalLooseNet = looseItems.stream().filter(l -> !l.isSold())
                .mapToDouble(l -> l.getNetWeight() == null ? 0.0 : l.getNetWeight()).sum();
        double totalLooseVar = looseItems.stream().filter(l -> !l.isSold())
                .mapToDouble(l -> l.getVariableWeight() == null ? 0.0 : l.getVariableWeight()).sum();

        // Update Box
        box.setNetWeight(totalPieceNet + totalLooseNet);
        box.setVariableWeight(totalPieceVar + totalLooseVar);
        box.setTotalPiece(totalPieceCount); // LooseItems don't increase count

        box.recalcGrossWeight();
        box.setUpdatedAt(LocalDateTime.now());
        repo.save(box);
    }
}
