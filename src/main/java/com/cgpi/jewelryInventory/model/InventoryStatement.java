package com.cgpi.jewelryInventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_statements")
public class InventoryStatement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String entityType;
	private Long entityId;

	private String barcode;
	private String pieceType;
	private String purity;

	private String looseItemName;

	private String action;

	private Long boxId;
	private Long fromBoxId;
	private Long toBoxId;

	private Double netWeightChange;
	private Double variableWeightChange;
	private Integer pieceCountChange;

	private String performedByRole;

	private LocalDateTime actionTime = LocalDateTime.now();

	private String remark;

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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getPieceType() {
		return pieceType;
	}

	public void setPieceType(String pieceType) {
		this.pieceType = pieceType;
	}

	public String getPurity() {
		return purity;
	}

	public void setPurity(String purity) {
		this.purity = purity;
	}

	public String getLooseItemName() {
		return looseItemName;
	}

	public void setLooseItemName(String looseItemName) {
		this.looseItemName = looseItemName;
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

	public LocalDateTime getActionTime() {
		return actionTime;
	}

	public void setActionTime(LocalDateTime actionTime) {
		this.actionTime = actionTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
