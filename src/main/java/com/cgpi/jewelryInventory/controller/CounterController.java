package com.cgpi.jewelryInventory.controller;

import com.cgpi.jewelryInventory.model.Counter;
import com.cgpi.jewelryInventory.service.CounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counter")
@CrossOrigin("*")
public class CounterController {

	private final CounterService service;

	public CounterController(CounterService service) {
		this.service = service;
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Counter> addCounter(@RequestBody Counter counter) {
		return ResponseEntity.ok(service.addCounter(counter));
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Counter> updateCounter(@RequestParam Long id, @RequestBody Counter counter) {
		return ResponseEntity.ok(service.updateCounter(id, counter));
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteCounter(@RequestParam Long id) {
		service.softDelete(id);
		return ResponseEntity.ok("Counter soft-deleted successfully");
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<Counter>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}

	@GetMapping("/getById")
	public ResponseEntity<Counter> getById(@RequestParam Long id) {
		return ResponseEntity.ok(service.getById(id));
	}
}
