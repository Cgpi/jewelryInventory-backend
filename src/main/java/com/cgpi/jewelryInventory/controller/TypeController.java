package com.cgpi.jewelryInventory.controller;

import com.cgpi.jewelryInventory.model.Type;
import com.cgpi.jewelryInventory.service.TypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
@CrossOrigin("*")
public class TypeController {

    private final TypeService service;

    public TypeController(TypeService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Type> addType(@RequestBody Type type) {
        return ResponseEntity.ok(service.addType(type));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Type>> getAllTypes() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/getById")
    public ResponseEntity<Type> getTypeById(@RequestParam Long id) {
        Type type = service.getAll().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Type not found"));
        return ResponseEntity.ok(type);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Type> updateType(@RequestParam Long id, @RequestBody Type type) {
        return ResponseEntity.ok(service.updateType(id, type));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteType(@RequestParam Long id) {
        service.softDelete(id);
        return ResponseEntity.ok("Type soft-deleted successfully");
    }
}
