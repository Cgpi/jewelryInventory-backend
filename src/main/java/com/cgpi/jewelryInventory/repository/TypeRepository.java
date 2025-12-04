package com.cgpi.jewelryInventory.repository;

import com.cgpi.jewelryInventory.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
    List<Type> findByDeletedFalse();
}
