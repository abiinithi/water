package com.example.water.billing;

public class BillingResult {
    private final long totalWater;
    private final long totalCost;

    public BillingResult(long totalWater, long totalCost) {
        this.totalWater = totalWater;
        this.totalCost = totalCost;
    }

    public long getTotalWater() { return totalWater; }
    public long getTotalCost() { return totalCost; }

    @Override
    public String toString() {
        return totalWater + " " + totalCost;
    }
}