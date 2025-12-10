package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.LooseItem;
import com.cgpi.jewelryInventory.repository.BoxRepository;
import com.cgpi.jewelryInventory.repository.LooseItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LooseItemService {

	private final LooseItemRepository looseItemRepository;
	private final BoxRepository boxRepository;
	private final BoxService boxService;
	private final InventoryStatementService statementService;

	public LooseItemService(
			LooseItemRepository looseItemRepository,
			BoxRepository boxRepository,
			BoxService boxService,
			InventoryStatementService statementService) {

		this.looseItemRepository = looseItemRepository;
		this.boxRepository = boxRepository;
		this.boxService = boxService;
		this.statementService = statementService;
	}

	@Transactional
	public LooseItem addLooseItem(LooseItem item) {
		boxRepository.findById(item.getBoxId())
				.orElseThrow(() -> new RuntimeException("Box not found"));

		if (item.getNetWeight() <= 0) {
			throw new RuntimeException("Net weight must be positive");
		}

		item.setVariableWeight(item.getNetWeight());
		item.setSold(false);
		item.setCreatedAt(LocalDateTime.now());
		item.setUpdatedAt(null);

		LooseItem saved = looseItemRepository.save(item);
		boxService.recalcBoxTotals(item.getBoxId());

		statementService.log(
				"LOOSE_ITEM",
				saved.getId(),
				"ADD",
				saved.getBoxId(),
				null,
				null,
				saved.getNetWeight(),
				saved.getVariableWeight(),
				0,
				"Loose item added"
		);

		return saved;
	}

	@Transactional
	public LooseItem updateLooseItem(Long id, LooseItem newItem) {
		LooseItem existing = getById(id);

		if (newItem.getNetWeight() <= 0) {
			throw new RuntimeException("Net weight must be positive");
		}

		double oldWeight = existing.getNetWeight();
		Long boxId = existing.getBoxId();

		existing.setName(newItem.getName());
		existing.setNetWeight(newItem.getNetWeight());
		existing.setVariableWeight(newItem.getNetWeight());
		existing.setUpdatedAt(LocalDateTime.now());

		LooseItem saved = looseItemRepository.save(existing);
		boxService.recalcBoxTotals(boxId);

		statementService.log(
				"LOOSE_ITEM",
				saved.getId(),
				"UPDATE",
				boxId,
				null,
				null,
				saved.getNetWeight() - oldWeight,
				saved.getVariableWeight() - oldWeight,
				0,
				"Loose item updated"
		);

		return saved;
	}

	public List<LooseItem> getAll() {
		return looseItemRepository.findAll();
	}

	public LooseItem getById(Long id) {
		return looseItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("LooseItem not found"));
	}

	public List<LooseItem> getByBoxId(Long boxId) {
		return looseItemRepository.findByBoxId(boxId);
	}

	@Transactional
	public LooseItem sellLooseItem(Long id, Double soldWeight) {
		LooseItem item = getById(id);

		if (soldWeight <= 0) {
			throw new RuntimeException("Sold weight must be positive");
		}
		if (item.isSold()) {
			throw new RuntimeException("Loose item already fully sold");
		}
		if (soldWeight > item.getNetWeight()) {
			throw new RuntimeException("Not enough weight available");
		}

		item.setNetWeight(item.getNetWeight() - soldWeight);
		item.setVariableWeight(item.getVariableWeight() - soldWeight);
		item.setUpdatedAt(LocalDateTime.now());

		if (item.getNetWeight() == 0) {
			item.setSold(true);
			item.setSoldAt(LocalDateTime.now());
		}

		LooseItem saved = looseItemRepository.save(item);
		boxService.recalcBoxTotals(item.getBoxId());

		statementService.log(
				"LOOSE_ITEM",
				saved.getId(),
				"PARTIAL_SELL",
				saved.getBoxId(),
				null,
				null,
				-soldWeight,
				-soldWeight,
				0,
				"Loose item sold by weight"
		);

		return saved;
	}

	@Transactional
	public LooseItem transferLooseItem(Long itemId, Long targetBoxId) {
		LooseItem item = getById(itemId);

		if (item.getBoxId().equals(targetBoxId)) {
			throw new RuntimeException("LooseItem already in this box");
		}

		boxRepository.findById(targetBoxId)
				.orElseThrow(() -> new RuntimeException("Target box not found"));

		Long oldBoxId = item.getBoxId();

		item.setBoxId(targetBoxId);
		item.setUpdatedAt(LocalDateTime.now());

		LooseItem saved = looseItemRepository.save(item);

		boxService.recalcBoxTotals(oldBoxId);
		boxService.recalcBoxTotals(targetBoxId);

		statementService.log(
				"LOOSE_ITEM",
				saved.getId(),
				"TRANSFER",
				null,
				oldBoxId,
				targetBoxId,
				0.0,
				0.0,
				0,
				"Loose item transferred"
		);

		return saved;
	}
}
