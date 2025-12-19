package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.Piece;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PieceRepository extends JpaRepository<Piece, Long> {
	List<Piece> findByBoxId(Long boxId);

	List<Piece> findBySoldTrue();

	List<Piece> findByBoxIdAndSoldTrue(Long boxId);
	
	List<Piece> findBySoldTrueAndSoldAtBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
