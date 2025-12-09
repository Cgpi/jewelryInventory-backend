package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.Counter;
import com.cgpi.jewelryInventory.repository.CounterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounterService {

	private final CounterRepository repo;

	public CounterService(CounterRepository repo) {
		this.repo = repo;
	}

	public Counter addCounter(Counter counter) {

		if (repo.existsByName(counter.getName()))
			throw new RuntimeException("Counter name already exists");

		return repo.save(counter);
	}

	public List<Counter> getAll() {
		return repo.findByDeletedFalse();
	}

	public Counter getById(Long id) {
		return repo.findById(id).filter(c -> !c.isDeleted())
				.orElseThrow(() -> new RuntimeException("Counter not found"));
	}

	public Counter updateCounter(Long id, Counter updated) {
		Counter existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Counter not found"));

		existing.setName(updated.getName());

		return repo.save(existing);
	}

	public void softDelete(Long id) {
		Counter counter = repo.findById(id).orElseThrow(() -> new RuntimeException("Counter not found"));

		counter.setDeleted(true);
		repo.save(counter);
	}
}
