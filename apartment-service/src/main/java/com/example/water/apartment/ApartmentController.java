package com.example.water.apartment;

import com.example.water.dto.BillRequestedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final ApartmentKafkaProducer kafkaProducer;

    public ApartmentController(ApartmentService apartmentService, ApartmentKafkaProducer kafkaProducer) {
        this.apartmentService = apartmentService;
        this.kafkaProducer = kafkaProducer;
    }

    // Maps to original: ALLOT_WATER <apartment-type> <ratio>
    @PostMapping("/{id}/allot")
    public ResponseEntity<String> allotWater(
            @PathVariable String id,
            @RequestParam String type,
            @RequestParam String ratio) {
        apartmentService.allotWater(id, type, ratio);
        return ResponseEntity.ok("Water successfully allotted for apartment: " + id);
    }

    // Maps to original: ADD_GUESTS <no_of_guests>
    @PostMapping("/{id}/guests")
    public ResponseEntity<String> addGuests(
            @PathVariable String id,
            @RequestParam int count) {
        apartmentService.addGuests(id, count);
        return ResponseEntity.ok(count + " guests added successfully to apartment: " + id);
    }

    // Maps to original: BILL (Triggers message generation step)
    @PostMapping("/{id}/bill")
    public ResponseEntity<String> requestBill(@PathVariable String id) {
        BillRequestedEvent event = apartmentService.generateBillRequest(id);
        kafkaProducer.publishBillRequest(event);
        return ResponseEntity.ok("Billing request sent to processing queue for apartment: " + id);
    }
}