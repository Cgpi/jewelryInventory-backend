package com.cgpi.jewelryInventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "boxes", uniqueConstraints = { @UniqueConstraint(columnNames = "identity") })
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, unique = true)
    private String identity;

    @Column(nullable = false)
    private Double fixedWeight;

    private Double netWeight = 0.0;
    private Double variableWeight = 0.0;
    private Double grossWeight = 0.0;

    private Integer totalPiece = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long counterId;

    // --- Constructors ---
    public Box() {}

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIdentity() { return identity; }
    public void setIdentity(String identity) { this.identity = identity; }

    public Double getFixedWeight() { return fixedWeight; }
    public void setFixedWeight(Double fixedWeight) { this.fixedWeight = fixedWeight; }

    public Double getNetWeight() { return netWeight; }
    public void setNetWeight(Double netWeight) { this.netWeight = netWeight; }

    public Double getVariableWeight() { return variableWeight; }
    public void setVariableWeight(Double variableWeight) { this.variableWeight = variableWeight; }

    public Double getGrossWeight() { return grossWeight; }
    public void setGrossWeight(Double grossWeight) { this.grossWeight = grossWeight; }

    public Integer getTotalPiece() { return totalPiece; }
    public void setTotalPiece(Integer totalPiece) { this.totalPiece = totalPiece; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getCounterId() { return counterId; }
    public void setCounterId(Long counterId) { this.counterId = counterId; }

    // Convenience: recalc gross weight from fixed + net
    public void recalcGrossWeight() {
        double f = fixedWeight == null ? 0.0 : fixedWeight;
        double n = netWeight == null ? 0.0 : netWeight;
        this.grossWeight = f + n;
    }
}
