package com.example.water.billing;

import com.example.water.dto.BillRequestedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillingKafkaListener {

    private final BillingCalculatorService calculatorService;

    public BillingKafkaListener(BillingCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @KafkaListener(topics = "water-billing-requests", groupId = "water-billing-group")
    public void consumeBillRequest(BillRequestedEvent event) {
        System.out.println("KAFKA CONSUMER: Received billing request for apartment ID: " + event.getApartmentId());
        // Execute calculations
        BillingResult result = calculatorService.calculateBill(event);
        
        // Print the output exactly as specified by the interview instructions: <TOTAL_WATER_CONSUMED_IN_LITERS> <TOTAL_COST>
        System.out.println("OUTPUT: " + result.toString()); // [cite: 38]
    }
}