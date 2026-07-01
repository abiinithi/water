package com.example.water.apartment;

import com.example.water.dto.BillRequestedEvent;
import org.springframework.stereotype.Service;

@Service
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;

    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public Apartment allotWater(String id, String type, String ratio) {
        validateRatio(ratio);
        Apartment apartment = new Apartment(id, type, ratio);
        apartmentRepository.save(apartment);
        return apartment;
    }

    public Apartment addGuests(String id, int guestCount) {
        if (guestCount <= 0) {
            throw new IllegalArgumentException("Guest count must be greater than zero.");
        }

        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apartment not found! Please execute allotment first."));
        apartment.addGuests(guestCount);
        apartmentRepository.save(apartment);
        return apartment;
    }

    public BillRequestedEvent generateBillRequest(String id) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apartment data missing."));

        return new BillRequestedEvent(
                apartment.getId(),
                apartment.getType(),
                apartment.getWaterRatio(),
                apartment.getTotalGuests()
        );
    }

    private void validateRatio(String ratio) {
        if (ratio == null || ratio.isBlank()) {
            throw new IllegalArgumentException("Water ratio must be in the format 'corporation:borewell' with positive numeric values.");
        }

        String[] ratioParts = ratio.split(":");
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
}