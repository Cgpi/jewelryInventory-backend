package com.cgpi.jewelryInventory.dto;

import java.time.LocalDate;

public class BoxResponse {
    private Long id;
    private String type;         // BOX or TRAY ✅ NEW
    private String identity;
    private Long counterId;
    private String counterName;
    private LocalDate date;
    private Double fixedWeight;
    private Double variableWeight;
    private Double grossWeight;
    private Double netWeight;
    private Integer totalPieces;
    private Double totalAll;     // ✅ NEW (net + variable + fixed)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIdentity() { return identity; }
    public void setIdentity(String identity) { this.identity = identity; }

    public Long getCounterId() { return counterId; }
    public void setCounterId(Long counterId) { this.counterId = counterId; }

    public String getCounterName() { return counterName; }
    public void setCounterName(String counterName) { this.counterName = counterName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getFixedWeight() { return fixedWeight; }
    public void setFixedWeight(Double fixedWeight) { this.fixedWeight = fixedWeight; }

    public Double getVariableWeight() { return variableWeight; }
    public void setVariableWeight(Double variableWeight) { this.variableWeight = variableWeight; }

    public Double getGrossWeight() { return grossWeight; }
    public void setGrossWeight(Double grossWeight) { this.grossWeight = grossWeight; }

    public Double getNetWeight() { return netWeight; }
    public void setNetWeight(Double netWeight) { this.netWeight = netWeight; }

    public Integer getTotalPieces() { return totalPieces; }
    public void setTotalPieces(Integer totalPieces) { this.totalPieces = totalPieces; }

    public Double getTotalAll() { return totalAll; }
    public void setTotalAll(Double totalAll) { this.totalAll = totalAll; }
}
