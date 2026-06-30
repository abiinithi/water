package com.example.water.apartment;

import com.example.water.dto.BillRequestedEvent;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ApartmentService {
    // In-memory store simulating our database layer
    private final Map<String, Apartment> apartmentStorage = new ConcurrentHashMap<>();

    public Apartment allotWater(String id, String type, String ratio) {
        Apartment apartment = new Apartment(id, type, ratio);
        apartmentStorage.put(id, apartment);
        return apartment;
    }

    public Apartment addGuests(String id, int guestCount) {
        Apartment apartment = apartmentStorage.get(id);
        if (apartment == null) {
            throw new IllegalArgumentException("Apartment not found! Please execute allotment first.");
        }
        apartment.addGuests(guestCount);
        return apartment;
    }

    public BillRequestedEvent generateBillRequest(String id) {
        Apartment apartment = apartmentStorage.get(id);
        if (apartment == null) {
            throw new IllegalArgumentException("Apartment data missing.");
        }

        // Map internal state into our shared transmission DTO
        return new BillRequestedEvent(
            apartment.getId(),
            apartment.getType(),
            apartment.getWaterRatio(),
            apartment.getTotalGuests()
        );
    }
}