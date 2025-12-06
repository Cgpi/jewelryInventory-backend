package com.cgpi.jewelryInventory.controller;

import com.cgpi.jewelryInventory.model.Piece;
import com.cgpi.jewelryInventory.service.PieceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pieces")
@CrossOrigin("*")
public class PieceController {

    private final PieceService service;

    public PieceController(PieceService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Piece> add(@RequestBody Piece piece) {
        return ResponseEntity.ok(service.addPiece(piece));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Piece> update(@RequestParam Long id, @RequestBody Piece piece) {
        return ResponseEntity.ok(service.updatePiece(id, piece));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Piece>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/getById")
    public ResponseEntity<Piece> getById(@RequestParam Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @GetMapping("/getByBoxId")
    public ResponseEntity<List<Piece>> getByBoxId(@RequestParam Long boxId) {
        return ResponseEntity.ok(service.getByBoxId(boxId));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNT')")
    public ResponseEntity<Piece> transfer(
            @RequestParam Long pieceId,
            @RequestParam Long boxId) {
        return ResponseEntity.ok(service.transferPiece(pieceId, boxId));
    }

    @PostMapping("/sold")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNT')")
    public ResponseEntity<Piece> sold(@RequestParam Long id) {
        return ResponseEntity.ok(service.markSold(id));
    }
}
