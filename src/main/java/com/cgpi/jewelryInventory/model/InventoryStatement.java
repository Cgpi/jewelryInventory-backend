package com.cgpi.jewelryInventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_statements")
public class InventoryStatement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String entityType;

	@Column(nullable = false)
	private Long entityId;

	@Column(nullable = false)
	private String action;

	private Long boxId;
	private Long fromBoxId;
	private Long toBoxId;

	private Double netWeightChange;
	private Double variableWeightChange;
	private Integer pieceCountChange;

	@Column(nullable = false)
	private String performedByRole;

	@Column(length = 1000)
	private String remark;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public InventoryStatement() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getBoxId() {
		return boxId;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}

	public Long getFromBoxId() {
		return fromBoxId;
	}

	public void setFromBoxId(Long fromBoxId) {
		this.fromBoxId = fromBoxId;
	}

	public Long getToBoxId() {
		return toBoxId;
	}

	public void setToBoxId(Long toBoxId) {
		this.toBoxId = toBoxId;
	}

	public Double getNetWeightChange() {
		return netWeightChange;
	}

	public void setNetWeightChange(Double netWeightChange) {
		this.netWeightChange = netWeightChange;
	}

	public Double getVariableWeightChange() {
		return variableWeightChange;
	}

	public void setVariableWeightChange(Double variableWeightChange) {
		this.variableWeightChange = variableWeightChange;
	}

	public Integer getPieceCountChange() {
		return pieceCountChange;
	}

	public void setPieceCountChange(Integer pieceCountChange) {
		this.pieceCountChange = pieceCountChange;
	}

	public String getPerformedByRole() {
		return performedByRole;
	}

	public void setPerformedByRole(String performedByRole) {
		this.performedByRole = performedByRole;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
