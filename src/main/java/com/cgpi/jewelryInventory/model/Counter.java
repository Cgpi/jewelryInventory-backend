package com.cgpi.jewelryInventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "counters")
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // UNIQUE FIELD

    @Column(nullable = false)
    private boolean deleted = false;

    public Counter() {}

    public Counter(String name) {
        this.name = name;
        this.deleted = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
