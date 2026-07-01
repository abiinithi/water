package com.example.water.billing;

import com.example.water.dto.BillRequestedEvent;
import org.springframework.stereotype.Service;

@Service
public class BillingCalculatorService {

    private static final int DAYS_IN_MONTH = 30; // All calculations assume a 30-day month [cite: 43]
    private static final int LITRES_PER_PERSON_DAY = 10; // Each person gets 10L daily [cite: 15]

    public BillingResult calculateBill(BillRequestedEvent event) {
        validateEvent(event);

        // 1. Determine base person count from apartment type [cite: 14]
        int basePeople = "2".equals(event.getApartmentType()) ? 3 : 5;
        int baseWaterAllocation = basePeople * LITRES_PER_PERSON_DAY * DAYS_IN_MONTH; // e.g., 900L or 1500L [cite: 15]

        // 2. Parse corporate-to-borewell allocation ratios [cite: 28]
        String[] ratioParts = event.getWaterRatio().split(":");
        double corpRatio = Double.parseDouble(ratioParts[0].trim());
        double boreRatio = Double.parseDouble(ratioParts[1].trim());
        double totalRatioParts = corpRatio + boreRatio;

        // Split total base consumption using the exact ratio values [cite: 16]
        double corporationWaterLitres = (corpRatio / totalRatioParts) * baseWaterAllocation;
        double borewellWaterLitres = (boreRatio / totalRatioParts) * baseWaterAllocation;

        // Apply flat billing rates [cite: 19, 20]
        double corporationCost = corporationWaterLitres * 1.0; // Rs. 1 per litre [cite: 19]
        double borewellCost = borewellWaterLitres * 1.5;       // Rs. 1.5 per litre [cite: 20]
        double baseCost = corporationCost + borewellCost;

        // 3. Compute supplementary guest/tanker water [cite: 17, 32]
        int totalGuestWaterLitres = event.getTotalGuests() * LITRES_PER_PERSON_DAY * DAYS_IN_MONTH;
        double tankerCost = calculateTankerSlabCost(totalGuestWaterLitres);

        // 4. Consolidate absolute aggregates rounded cleanly to the nearest whole integer [cite: 45]
        long totalWaterConsumed = Math.round(baseWaterAllocation + totalGuestWaterLitres);
        long totalCalculatedCost = Math.round(baseCost + tankerCost);

        return new BillingResult(totalWaterConsumed, totalCalculatedCost);
    }

    private void validateEvent(BillRequestedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Billing request must not be null.");
        }

        if (!"2".equals(event.getApartmentType()) && !"3".equals(event.getApartmentType())) {
            throw new IllegalArgumentException("Apartment type must be either '2' or '3'.");
        }

        if (event.getWaterRatio() == null || event.getWaterRatio().isBlank()) {
            throw new IllegalArgumentException("Water ratio must be in the format 'corporation:borewell' with positive numeric values.");
        }

        String[] ratioParts = event.getWaterRatio().split(":");
        if (ratioParts.length != 2) {
            throw new IllegalArgumentException("Water ratio must be in the format 'corporation:borewell' with positive numeric values.");
        }

        try {
            double corporationRatio = Double.parseDouble(ratioParts[0].trim());
            double borewellRatio = Double.parseDouble(ratioParts[1].trim());
            if (corporationRatio <= 0 || borewellRatio <= 0) {
                throw new IllegalArgumentException("Water ratio must be in the format 'corporation:borewell' with positive numeric values.");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Water ratio must be in the format 'corporation:borewell' with positive numeric values.", ex);
        }
    }

    // Process excess tanker water requirements utilizing a step-down slab breakdown [cite: 20, 21, 22, 23]
    private double calculateTankerSlabCost(int litres) {
        double accumulatedCost = 0;
        if (litres <= 0) return accumulatedCost;

        // Slab Step 1: First 500L @ Rs. 2 per Litre [cite: 21]
        if (litres <= 500) return litres * 2.0;
        accumulatedCost += 500 * 2.0;
        litres -= 500;

        // Slab Step 2: Next 1000L (501L to 1500L) @ Rs. 3 per Litre [cite: 22]
        if (litres <= 1000) return accumulatedCost + (litres * 3.0);
        accumulatedCost += 1000 * 3.0;
        litres -= 1000;

        // Slab Step 3: Next 1500L (1501L to 3000L) @ Rs. 5 per Litre [cite: 23]
        if (litres <= 1500) return accumulatedCost + (litres * 5.0);
        accumulatedCost += 1500 * 5.0;
        litres -= 1500;

        // Slab Step 4: Overflow volume (3001L+) @ Rs. 8 per Litre [cite: 23]
        accumulatedCost += litres * 8.0;
        return accumulatedCost;
    }
}