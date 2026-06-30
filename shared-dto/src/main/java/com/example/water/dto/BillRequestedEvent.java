package com.example.water.dto;

import java.io.Serializable;

public class BillRequestedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String apartmentId;
    private String apartmentType; // "2" or "3" BHK
    private String waterRatio;     // e.g., "1:2"
    private int totalGuests;

    public BillRequestedEvent() {}

    public BillRequestedEvent(String apartmentId, String apartmentType, String waterRatio, int totalGuests) {
        this.apartmentId = apartmentId;
        this.apartmentType = apartmentType;
        this.waterRatio = waterRatio;
        this.totalGuests = totalGuests;
    }

    // Getters and Setters
    public String getApartmentId() { return apartmentId; }
    public void setApartmentId(String apartmentId) { this.apartmentId = apartmentId; }

    public String getApartmentType() { return apartmentType; }
    public void setApartmentType(String apartmentType) { this.apartmentType = apartmentType; }

    public String getWaterRatio() { return waterRatio; }
    public void setWaterRatio(String waterRatio) { this.waterRatio = waterRatio; }

    public int getTotalGuests() { return totalGuests; }
    public void setTotalGuests(int totalGuests) { this.totalGuests = totalGuests; }
}