package com.cgpi.jewelryInventory.controller;

import com.cgpi.jewelryInventory.model.LooseItem;
import com.cgpi.jewelryInventory.service.LooseItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loose")
@CrossOrigin("*")
public class LooseItemController {

	private final LooseItemService service;

	public LooseItemController(LooseItemService service) {
		this.service = service;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<LooseItem> add(@RequestBody LooseItem item) {
		return ResponseEntity.ok(service.addLooseItem(item));
	}

	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<LooseItem> update(@RequestParam Long id, @RequestBody LooseItem item) {
		return ResponseEntity.ok(service.updateLooseItem(id, item));
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<LooseItem>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}

	@GetMapping("/getById")
	public ResponseEntity<LooseItem> getById(@RequestParam Long id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@GetMapping("/getByBoxId")
	public ResponseEntity<List<LooseItem>> getByBoxId(@RequestParam Long boxId) {
		return ResponseEntity.ok(service.getByBoxId(boxId));
	}

	@PostMapping("/sell")
	@PreAuthorize("hasAnyRole('ADMIN','ACCOUNT')")
	public ResponseEntity<LooseItem> sell(@RequestParam Long id, @RequestParam Double weight) {
		return ResponseEntity.ok(service.sellLooseItem(id, weight));
	}

	@PostMapping("/transfer")
	@PreAuthorize("hasAnyRole('ADMIN','ACCOUNT')")
	public ResponseEntity<LooseItem> transfer(@RequestParam Long itemId, @RequestParam Long boxId) {
		return ResponseEntity.ok(service.transferLooseItem(itemId, boxId));
	}
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> delete(@RequestParam Long id) {
		service.deleteLooseItem(id);
		return ResponseEntity.ok("Loose item deleted successfully");
	}
}
