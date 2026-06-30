package com.example.water.apartment;

import com.example.water.dto.BillRequestedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ApartmentKafkaProducer {

    private static final String TOPIC = "water-billing-requests";
    private final KafkaTemplate<String, BillRequestedEvent> kafkaTemplate;

    public ApartmentKafkaProducer(KafkaTemplate<String, BillRequestedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBillRequest(BillRequestedEvent event) {
        // Use the apartment ID as the Kafka partitioning key to keep order safe
        kafkaTemplate.send(TOPIC, event.getApartmentId(), event)
            .whenComplete((result, exception) -> {
                if (exception != null) {
                    System.err.println("KAFKA PRODUCER ERROR: Failed to send message!");
                    System.err.println("Reason: " + exception.getMessage());
                    exception.printStackTrace(); // Prints the exact line breaking your chain
                } else {
                    System.out.println("KAFKA PRODUCER SUCCESS: Message sent to topic " + TOPIC 
                        + " [Partition: " + result.getRecordMetadata().partition() 
                        + ", Offset: " + result.getRecordMetadata().offset() + "]");
                }
            });
    }
}