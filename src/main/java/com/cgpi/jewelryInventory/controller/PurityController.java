package com.cgpi.jewelryInventory.controller;

import com.cgpi.jewelryInventory.model.Purity;
import com.cgpi.jewelryInventory.service.PurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purity")
@CrossOrigin("*")
public class PurityController {

    private final PurityService service;

    public PurityController(PurityService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Purity> addPurity(@RequestBody Purity purity) {
        return ResponseEntity.ok(service.addPurity(purity));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Purity>> getAllPurities() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/getById")
    public ResponseEntity<Purity> getPurityById(@RequestParam Long id) {
        Purity purity = service.getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Purity not found"));
        return ResponseEntity.ok(purity);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Purity> updatePurity(@RequestParam Long id, @RequestBody Purity purity) {
        return ResponseEntity.ok(service.updatePurity(id, purity));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePurity(@RequestParam Long id) {
        service.softDelete(id);
        return ResponseEntity.ok("Purity soft-deleted successfully");
    }
}
