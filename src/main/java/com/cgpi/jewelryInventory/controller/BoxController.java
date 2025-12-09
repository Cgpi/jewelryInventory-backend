package com.cgpi.jewelryInventory.controller;

import com.cgpi.jewelryInventory.model.Box;
import com.cgpi.jewelryInventory.service.BoxService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/box")
@CrossOrigin("*")
public class BoxController {

	private final BoxService service;

	public BoxController(BoxService service) {
		this.service = service;
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Box> addBox(@RequestBody Box box) {
		return ResponseEntity.ok(service.createBox(box));
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<Box>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}

	@GetMapping("/getById")
	public ResponseEntity<Box> getById(@RequestParam Long id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@GetMapping("/getByCounterId")
	public ResponseEntity<List<Box>> getByCounterId(@RequestParam Long counterId) {
		return ResponseEntity.ok(service.getByCounterId(counterId));
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Box> updateBox(@RequestParam Long id, @RequestBody Box box) {
		return ResponseEntity.ok(service.updateBox(id, box));
	}

	@PostMapping("/transfer")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Box> transferBox(@RequestParam Long boxId, @RequestParam Long counterId) {
		return ResponseEntity.ok(service.transferBox(boxId, counterId));
	}
}
