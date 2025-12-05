package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.Piece;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PieceRepository extends JpaRepository<Piece, Long> {
    List<Piece> findByBoxId(Long boxId);
}
