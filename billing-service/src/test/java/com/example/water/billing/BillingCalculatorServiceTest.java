package com.example.water.billing;

import com.example.water.dto.BillRequestedEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BillingCalculatorServiceTest {

    private final BillingCalculatorService calculatorService = new BillingCalculatorService();

    @Test
    public void testExampleOne_BaseAllotmentNoGuests() {
        // Input matching Example #1: 2 BHK, Ratio 1:2, No Guests [cite: 53, 54]
        BillRequestedEvent event = new BillRequestedEvent("Apt-101", "2", "1:2", 0);
        
        BillingResult result = calculatorService.calculateBill(event);

        // Validating output formatting matches 900L consumed, 1200 Rs Cost [cite: 56]
        assertEquals(900, result.getTotalWater());
        assertEquals(1200, result.getTotalCost());
    }

    @Test
    public void testExampleTwo_AccumulatedGuests() {
        // Input matching Example #2: 2 BHK, Ratio 3:7, 5 Total Guests [cite: 59, 60, 61]
        BillRequestedEvent event = new BillRequestedEvent("Apt-102", "2", "3:7", 5);
        
        BillingResult result = calculatorService.calculateBill(event);

        // Validating output formatting matches 2400L consumed, 5215 Rs Cost [cite: 64]
        assertEquals(2400, result.getTotalWater());
        assertEquals(5215, result.getTotalCost());
    }

    @Test
    public void testRejectsUnsupportedApartmentType() {
        BillRequestedEvent event = new BillRequestedEvent("Apt-103", "4", "1:1", 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculatorService.calculateBill(event)
        );

        assertEquals("Apartment type must be either '2' or '3'.", exception.getMessage());
    }

    @Test
    public void testRejectsInvalidWaterRatio() {
        BillRequestedEvent event = new BillRequestedEvent("Apt-104", "3", "1:0", 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculatorService.calculateBill(event)
        );

        assertEquals("Water ratio must be in the format 'corporation:borewell' with positive numeric values.", exception.getMessage());
    }
}