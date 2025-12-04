package com.cgpi.jewelryInventory.dto;

import java.time.LocalDate;

public class BoxRequest {
    private String type;        // BOX or TRAY ✅ NEW
    private String identity;
    private Long counterId;
    private LocalDate date;
    private Double fixedWeight;

    // In request, variable fields (net, gross, variableWeight, totals) 
    // should NOT be passed, they’re auto-calculated.

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIdentity() { return identity; }
    public void setIdentity(String identity) { this.identity = identity; }

    public Long getCounterId() { return counterId; }
    public void setCounterId(Long counterId) { this.counterId = counterId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getFixedWeight() { return fixedWeight; }
    public void setFixedWeight(Double fixedWeight) { this.fixedWeight = fixedWeight; }
}
