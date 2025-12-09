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

    public LooseItemService(LooseItemRepository looseItemRepository, BoxRepository boxRepository, BoxService boxService) {
        this.looseItemRepository = looseItemRepository;
        this.boxRepository = boxRepository;
        this.boxService = boxService;
    }

    @Transactional
    public LooseItem addLooseItem(LooseItem item) {
        boxRepository.findById(item.getBoxId()).orElseThrow(() -> new RuntimeException("Box not found"));

        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(null);
        item.setSold(false);

        LooseItem saved = looseItemRepository.save(item);

        boxService.recalcBoxTotals(item.getBoxId());
        return saved;
    }

    @Transactional
    public LooseItem updateLooseItem(Long id, LooseItem newItem) {
        LooseItem existing = looseItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LooseItem not found"));

        Long boxId = existing.getBoxId();

        existing.setName(newItem.getName());
        existing.setNetWeight(newItem.getNetWeight());
        existing.setVariableWeight(newItem.getVariableWeight());
        existing.setUpdatedAt(LocalDateTime.now());

        LooseItem saved = looseItemRepository.save(existing);

        boxService.recalcBoxTotals(boxId);
        return saved;
    }

    public List<LooseItem> getAll() { return looseItemRepository.findAll(); }
    public LooseItem getById(Long id) { return looseItemRepository.findById(id).orElseThrow(() -> new RuntimeException("LooseItem not found")); }
    public List<LooseItem> getByBoxId(Long boxId) { return looseItemRepository.findByBoxId(boxId); }

    @Transactional
    public LooseItem sellLooseItem(Long id, Double soldWeight) {
        LooseItem item = getById(id);

        if (soldWeight <= 0) {
            throw new RuntimeException("Sold weight must be positive");
        }

        if (item.isSold() || soldWeight > item.getNetWeight()) {
            throw new RuntimeException("Not enough weight available or item already sold");
        }

        // Deduct the sold weight
        item.setNetWeight(item.getNetWeight() - soldWeight);
        item.setUpdatedAt(LocalDateTime.now());

        // If netWeight becomes 0, mark fully sold
        if (item.getNetWeight() == 0) {
            item.setSold(true);
            item.setSoldAt(LocalDateTime.now());
        }

        LooseItem saved = looseItemRepository.save(item);

        // Update box totals (netWeight & variableWeight), totalPiece not affected
        boxService.recalcBoxTotals(item.getBoxId());

        return saved;
    }

    @Transactional
    public LooseItem transferLooseItem(Long itemId, Long targetBoxId) {
        LooseItem item = getById(itemId);

        if (item.getBoxId().equals(targetBoxId)) {
            throw new RuntimeException("LooseItem is already in this box");
        }

        // Verify target box exists
        boxRepository.findById(targetBoxId)
                .orElseThrow(() -> new RuntimeException("Target box not found"));

        Long oldBoxId = item.getBoxId();
        item.setBoxId(targetBoxId);
        item.setUpdatedAt(LocalDateTime.now());

        LooseItem saved = looseItemRepository.save(item);

        // Recalculate totals for both old and new boxes
        boxService.recalcBoxTotals(oldBoxId);
        boxService.recalcBoxTotals(targetBoxId);

        return saved;
    }

}
