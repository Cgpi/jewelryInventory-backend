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

	public LooseItemService(LooseItemRepository looseItemRepository, BoxRepository boxRepository, BoxService boxService,
			InventoryStatementService statementService) {

		this.looseItemRepository = looseItemRepository;
		this.boxRepository = boxRepository;
		this.boxService = boxService;
		this.statementService = statementService;
	}


	private double round3(double value) {
		return Math.round(value * 1000.0) / 1000.0;
	}

	@Transactional
	public LooseItem addLooseItem(LooseItem item) {

		boxRepository.findById(item.getBoxId()).orElseThrow(() -> new RuntimeException("Box not found"));

		if (item.getNetWeight() == null || item.getNetWeight() <= 0) {
			throw new RuntimeException("Net weight must be positive");
		}

		item.setNetWeight(round3(item.getNetWeight()));
		item.setVariableWeight(round3(item.getVariableWeight()));
		item.setSold(false);
		item.setCreatedAt(LocalDateTime.now());
		item.setUpdatedAt(null);

		LooseItem saved = looseItemRepository.save(item);
		boxService.recalcBoxTotals(saved.getBoxId());

		statementService.logLoose(saved, "ADDED_LOOSE", null, null, saved.getNetWeight(), saved.getVariableWeight(),
				"Loose item added");

		return saved;
	}

	@Transactional
	public LooseItem updateLooseItem(Long id, LooseItem newItem) {

		LooseItem existing = getById(id);
		Long boxId = existing.getBoxId();

		double oldNet = existing.getNetWeight();
		double oldVar = existing.getVariableWeight();

		existing.setName(newItem.getName());
		existing.setNetWeight(round3(newItem.getNetWeight()));
		existing.setVariableWeight(round3(newItem.getVariableWeight()));
		existing.setUpdatedAt(LocalDateTime.now());

		LooseItem saved = looseItemRepository.save(existing);
		boxService.recalcBoxTotals(boxId);

		statementService.logLoose(saved, "UPDATED_LOOSE", null, null, round3(saved.getNetWeight() - oldNet),
				round3(saved.getVariableWeight() - oldVar), "Loose item updated");

		return saved;
	}

	public List<LooseItem> getAll() {
		return looseItemRepository.findAll();
	}

	public LooseItem getById(Long id) {
		return looseItemRepository.findById(id).orElseThrow(() -> new RuntimeException("LooseItem not found"));
	}

	public List<LooseItem> getByBoxId(Long boxId) {
		return looseItemRepository.findByBoxId(boxId);
	}

	// ✅ FIXED METHOD (NO FLOATING GARBAGE)
	@Transactional
	public LooseItem sellLooseItem(Long id, Double soldWeight) {

		if (soldWeight == null || soldWeight <= 0) {
			throw new RuntimeException("Sold weight must be positive");
		}

		LooseItem item = getById(id);

		double newNet = round3(item.getNetWeight() - soldWeight);
		double newVar = round3(item.getVariableWeight() - soldWeight);

		if (newNet < 0) {
			throw new RuntimeException("Sold weight exceeds available weight");
		}

		item.setNetWeight(newNet);
		item.setVariableWeight(newVar);
		item.setUpdatedAt(LocalDateTime.now());

		if (newNet == 0.0) {
			item.setSold(true);
			item.setSoldAt(LocalDateTime.now());
		}

		LooseItem saved = looseItemRepository.save(item);
		boxService.recalcBoxTotals(saved.getBoxId());

		statementService.logLoose(saved, "PARTIAL_SELL", null, null, round3(-soldWeight), round3(-soldWeight),
				"Loose item sold by weight");

		return saved;
	}

	@Transactional
	public LooseItem transferLooseItem(Long itemId, Long targetBoxId) {

		LooseItem item = getById(itemId);

		boxRepository.findById(targetBoxId).orElseThrow(() -> new RuntimeException("Target box not found"));

		Long oldBoxId = item.getBoxId();
		double weight = item.getNetWeight();

		item.setBoxId(targetBoxId);
		item.setUpdatedAt(LocalDateTime.now());

		LooseItem saved = looseItemRepository.save(item);

		statementService.logLoose(saved, "TRANSFER_OUT", oldBoxId, targetBoxId, round3(-weight), round3(-weight),
				"Loose item transferred out");

		statementService.logLoose(saved, "TRANSFER_IN", oldBoxId, targetBoxId, round3(weight), round3(weight),
				"Loose item transferred in");

		boxService.recalcBoxTotals(oldBoxId);
		boxService.recalcBoxTotals(targetBoxId);

		return saved;
	}

	@Transactional
	public void deleteLooseItem(Long id) {

		LooseItem item = getById(id);

		Long boxId = item.getBoxId();

		statementService.logLoose(item, "DELETE", boxId, null, round3(-item.getNetWeight()),
				round3(-item.getVariableWeight()), "Loose item deleted");

		looseItemRepository.delete(item);
		boxService.recalcBoxTotals(boxId);
	}
}
