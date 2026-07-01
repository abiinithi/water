package com.example.water.billing;

import com.example.water.dto.BillRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillingKafkaListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingKafkaListener.class);

    private final BillingCalculatorService calculatorService;

    public BillingKafkaListener(BillingCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @KafkaListener(topics = "${water.kafka.billing-request-topic:water-billing-requests}", groupId = "${water.kafka.billing-consumer-group:water-billing-group}")
    public void consumeBillRequest(BillRequestedEvent event) {
        LOGGER.info("Received billing request for apartment {}", event.getApartmentId());
        try {
            BillingResult result = calculatorService.calculateBill(event);
            LOGGER.info("Billing output for apartment {}: {}", event.getApartmentId(), result);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Unable to process billing request for apartment {}", event.getApartmentId(), ex);
        }
    }
}