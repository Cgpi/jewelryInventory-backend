package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.Piece;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PieceRepository extends JpaRepository<Piece, Long> {
}
