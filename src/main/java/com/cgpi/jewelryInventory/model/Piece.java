package com.cgpi.jewelryInventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pieces")
public class Piece {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// barcode is OPTIONAL and NOT unique
	private String barcode;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private String purity;

	@Column(nullable = false)
	private Double netWeight;

	@Column(nullable = false)
	private Double variableWeight;

	@Column(nullable = false)
	private Long boxId;

	@Column(nullable = false)
	private boolean sold = false;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPurity() {
		return purity;
	}

	public void setPurity(String purity) {
		this.purity = purity;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getVariableWeight() {
		return variableWeight;
	}

	public void setVariableWeight(Double variableWeight) {
		this.variableWeight = variableWeight;
	}

	public Long getBoxId() {
		return boxId;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
