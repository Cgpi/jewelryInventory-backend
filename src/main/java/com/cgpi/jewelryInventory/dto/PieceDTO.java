package com.cgpi.jewelryInventory.dto;

import java.time.LocalDate;

public class PieceDTO {
	private Long id;
	private LocalDate date; // entry date
	private Long counterId;
	private String counterName;
	private Long boxId;
	private String boxIdentity;
	private String barcode;
	private String type;
	private Double weight;
	private Double vweight;
	private String status;
	private LocalDate soldDate; // new field

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getCounterId() {
		return counterId;
	}

	public void setCounterId(Long counterId) {
		this.counterId = counterId;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public Long getBoxId() {
		return boxId;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}

	public String getBoxIdentity() {
		return boxIdentity;
	}

	public void setBoxIdentity(String boxIdentity) {
		this.boxIdentity = boxIdentity;
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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getVweight() {
		return vweight;
	}

	public void setVweight(Double vweight) {
		this.vweight = vweight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getSoldDate() {
		return soldDate;
	}

	public void setSoldDate(LocalDate soldDate) {
		this.soldDate = soldDate;
	}

}
