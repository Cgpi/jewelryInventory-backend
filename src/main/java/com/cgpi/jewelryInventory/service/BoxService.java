package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.Box;
import com.cgpi.jewelryInventory.repository.BoxRepository;
import com.cgpi.jewelryInventory.repository.CounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoxService {

    private final BoxRepository repo;
    private final CounterRepository counterRepository;

    public BoxService(BoxRepository repo, CounterRepository counterRepository) {
        this.repo = repo;
        this.counterRepository = counterRepository;
    }

    /**
     * Create a new box. Admin-entered fields only: counterId, type, identity, fixedWeight.
     * Initializes calculated fields (net/variable/gross/totalPiece).
     */
    public Box createBox(Box box) {
        if (box.getCounterId() == null) {
            throw new RuntimeException("counterId is required");
        }

        // ensure counter exists
        counterRepository.findById(box.getCounterId())
                .orElseThrow(() -> new RuntimeException("Counter not found"));

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

    /**
     * Admin-updatable fields: type, identity, fixedWeight.
     * Calculated fields are preserved / recalculated (grossWeight uses netWeight).
     */
    public Box updateBox(Long id, Box newBox) {
        Box box = getById(id);

        if (newBox.getIdentity() == null) {
            throw new RuntimeException("identity is required");
        }

        if (!box.getIdentity().equals(newBox.getIdentity())
                && repo.existsByIdentity(newBox.getIdentity())) {
            throw new RuntimeException("Identity already exists");
        }

        // Only admin-editable fields:
        box.setType(newBox.getType());
        box.setIdentity(newBox.getIdentity());
        if (newBox.getFixedWeight() != null) {
            box.setFixedWeight(newBox.getFixedWeight());
        }

        // Recalculate gross weight from fixed + net (net is not admin-editable)
        box.recalcGrossWeight();

        box.setUpdatedAt(LocalDateTime.now());
        return repo.save(box);
    }

    public List<Box> getAll() {
        return repo.findAll();
    }

    public Box getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Box not found"));
    }

    @Transactional
    public Box transferBox(Long boxId, Long targetCounterId) {
        Box box = getById(boxId);

        if (box.getCounterId().equals(targetCounterId)) {
            throw new RuntimeException("Box is already in this counter");
        }

        counterRepository.findById(targetCounterId)
                .orElseThrow(() -> new RuntimeException("Target counter not found"));

        box.setCounterId(targetCounterId);
        box.setUpdatedAt(LocalDateTime.now());

        return repo.save(box);
    }
}
