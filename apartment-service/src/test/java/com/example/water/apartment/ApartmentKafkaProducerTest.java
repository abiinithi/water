package com.example.water.apartment;

import com.example.water.dto.BillRequestedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApartmentKafkaProducerTest {

    @Mock
    private KafkaTemplate<String, BillRequestedEvent> kafkaTemplate;

    @InjectMocks
    private ApartmentKafkaProducer producer;

    @Test
    void publishBillRequestSendsEventToConfiguredTopic() {
        BillRequestedEvent event = new BillRequestedEvent("Apt-1", "2", "1:2", 0);
        CompletableFuture<SendResult<String, BillRequestedEvent>> future = CompletableFuture.completedFuture(new SendResult<>(null, null));
        when(kafkaTemplate.send(any(), eq("Apt-1"), any(BillRequestedEvent.class))).thenReturn(future);

        producer.publishBillRequest(event);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BillRequestedEvent> eventCaptor = ArgumentCaptor.forClass(BillRequestedEvent.class);
        verify(kafkaTemplate).send(topicCaptor.capture(), eq("Apt-1"), eventCaptor.capture());

        assertEquals("water-billing-requests", topicCaptor.getValue());
        assertEquals(event, eventCaptor.getValue());
    }
}
