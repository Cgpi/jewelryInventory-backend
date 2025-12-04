package com.cgpi.jewelryInventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "purities")
public class Purity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String purity;

    @Column(nullable = false)
    private boolean deleted = false;

    public Purity() {}

    public Purity(String purity) {
        this.purity = purity;
        this.deleted = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPurity() { return purity; }
    public void setPurity(String purity) { this.purity = purity; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
