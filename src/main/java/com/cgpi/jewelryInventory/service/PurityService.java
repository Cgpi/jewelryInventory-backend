package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.Purity;
import com.cgpi.jewelryInventory.repository.PurityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PurityService {

	private final PurityRepository repo;

	public PurityService(PurityRepository repo) {
		this.repo = repo;
	}

	public Purity addPurity(Purity purity) {
		return repo.save(purity);
	}

	public List<Purity> getAll() {
		return repo.findByDeletedFalse();
	}

	public Purity updatePurity(Long id, Purity newPurity) {
		Purity existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Purity not found"));
		existing.setPurity(newPurity.getPurity());
		return repo.save(existing);
	}

	public void softDelete(Long id) {
		Purity purity = repo.findById(id).orElseThrow(() -> new RuntimeException("Purity not found"));
		purity.setDeleted(true);
		repo.save(purity);
	}
}
