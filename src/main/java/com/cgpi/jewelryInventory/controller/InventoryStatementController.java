package com.cgpi.jewelryInventory.controller;

import com.cgpi.jewelryInventory.model.InventoryStatement;
import com.cgpi.jewelryInventory.repository.InventoryStatementRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statements")
@CrossOrigin("*")
public class InventoryStatementController {

	private final InventoryStatementRepository repo;

	public InventoryStatementController(InventoryStatementRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/byBox")
	@PreAuthorize("hasAnyRole('ADMIN','ACCOUNT')")
	public List<InventoryStatement> byBox(@RequestParam Long boxId) {
		return repo.findByBoxId(boxId);
	}
}
