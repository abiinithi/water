package com.example.water.apartment;

import com.example.water.dto.BillRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ApartmentKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentKafkaProducer.class);

    private final KafkaTemplate<String, BillRequestedEvent> kafkaTemplate;
    private final String billingRequestTopic;

    public ApartmentKafkaProducer(
            KafkaTemplate<String, BillRequestedEvent> kafkaTemplate,
            @Value("${water.kafka.billing-request-topic:water-billing-requests}") String billingRequestTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.billingRequestTopic = (billingRequestTopic == null || billingRequestTopic.isBlank())
                ? "water-billing-requests"
                : billingRequestTopic;
    }

    public void publishBillRequest(BillRequestedEvent event) {
        kafkaTemplate.send(billingRequestTopic, event.getApartmentId(), event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        LOGGER.error("Failed to publish billing request for apartment {} to topic {}", event.getApartmentId(), billingRequestTopic, exception);
                    } else {
                        LOGGER.info("Published billing request for apartment {} to topic {} [partition={}, offset={}]",
                                event.getApartmentId(), billingRequestTopic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    }
                });
    }
}