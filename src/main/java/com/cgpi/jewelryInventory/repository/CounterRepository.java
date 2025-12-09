package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CounterRepository extends JpaRepository<Counter, Long> {
	List<Counter> findByDeletedFalse();

	boolean existsByName(String name);
}
