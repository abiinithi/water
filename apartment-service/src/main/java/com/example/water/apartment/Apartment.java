package com.example.water.apartment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    private String id;
    private String type; // "2" or "3"
    private String waterRatio; // "Corporation:Borewell"
    private int totalGuests;

    protected Apartment() {
        // Required by JPA
    }

    public Apartment(String id, String type, String waterRatio) {
        this.id = id;
        this.type = type;
        this.waterRatio = waterRatio;
        this.totalGuests = 0;
    }

    public void addGuests(int count) {
        this.totalGuests += count;
    }

    // Getters
    public String getId() { return id; }
    public String getType() { return type; }
    public String getWaterRatio() { return waterRatio; }
    public int getTotalGuests() { return totalGuests; }
}