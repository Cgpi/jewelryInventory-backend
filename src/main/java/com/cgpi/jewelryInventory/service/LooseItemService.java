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

	@Transactional
	public LooseItem addLooseItem(LooseItem item) {

		boxRepository.findById(item.getBoxId()).orElseThrow(() -> new RuntimeException("Box not found"));

		if (item.getNetWeight() <= 0) {
			throw new RuntimeException("Net weight must be positive");
		}

		item.setVariableWeight(item.getVariableWeight());
		item.setSold(false);
		item.setCreatedAt(LocalDateTime.now());
		item.setUpdatedAt(null);

		LooseItem saved = looseItemRepository.save(item);
		boxService.recalcBoxTotals(saved.getBoxId());

		statementService.logLoose(saved, "ADD", null, null, saved.getNetWeight(), saved.getVariableWeight(),
				"Loose item added");

		return saved;
	}

	@Transactional
	public LooseItem updateLooseItem(Long id, LooseItem newItem) {

		LooseItem existing = getById(id);

		double oldWeight = existing.getNetWeight();
		Long boxId = existing.getBoxId();

		existing.setName(newItem.getName());
		existing.setNetWeight(newItem.getNetWeight());
		existing.setVariableWeight(newItem.getVariableWeight());
		existing.setUpdatedAt(LocalDateTime.now());

		LooseItem saved = looseItemRepository.save(existing);
		boxService.recalcBoxTotals(boxId);

		statementService.logLoose(saved, "UPDATE", null, null, saved.getNetWeight() - oldWeight,
				saved.getVariableWeight() - oldWeight, "Loose item updated");

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

	@Transactional
	public LooseItem sellLooseItem(Long id, Double soldWeight) {

		LooseItem item = getById(id);

		item.setNetWeight(item.getNetWeight() - soldWeight);
		item.setVariableWeight(item.getVariableWeight() - soldWeight);
		item.setUpdatedAt(LocalDateTime.now());

		if (item.getNetWeight() == 0) {
			item.setSold(true);
			item.setSoldAt(LocalDateTime.now());
		}

		LooseItem saved = looseItemRepository.save(item);
		boxService.recalcBoxTotals(saved.getBoxId());

		statementService.logLoose(saved, "PARTIAL_SELL", null, null, -soldWeight, -soldWeight,
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

		statementService.logLoose(saved, "TRANSFER_OUT", oldBoxId, targetBoxId, -weight, -weight,
				"Loose item transferred out");

		statementService.logLoose(saved, "TRANSFER_IN", oldBoxId, targetBoxId, weight, weight,
				"Loose item transferred in");

		boxService.recalcBoxTotals(oldBoxId);
		boxService.recalcBoxTotals(targetBoxId);

		return saved;
	}

	@Transactional
	public void deleteLooseItem(Long id) {

		LooseItem item = getById(id);

		Long boxId = item.getBoxId();
		double weight = item.getNetWeight();
		double variableWeight = item.getVariableWeight();

		statementService.logLoose(item, "DELETE", boxId, null, -weight, -variableWeight, "Loose item deleted");

		looseItemRepository.delete(item);

		boxService.recalcBoxTotals(boxId);
	}

}
