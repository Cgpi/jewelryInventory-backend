package com.cgpi.jewelryInventory.service;

import com.cgpi.jewelryInventory.model.Type;
import com.cgpi.jewelryInventory.repository.TypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TypeService {

    private final TypeRepository repo;

    public TypeService(TypeRepository repo) {
        this.repo = repo;
    }

    public Type addType(Type type) {
        return repo.save(type);
    }

    public List<Type> getAll() {
        return repo.findByDeletedFalse();
    }

    public Type updateType(Long id, Type newType) {
        Type existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Type not found"));
        existing.setName(newType.getName());
        return repo.save(existing);
    }

    public void softDelete(Long id) {
        Type type = repo.findById(id).orElseThrow(() -> new RuntimeException("Type not found"));
        type.setDeleted(true);
        repo.save(type);
    }
}
