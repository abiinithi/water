package com.example.water.apartment;

import com.example.water.dto.BillRequestedEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApartmentServiceTest {

    private final ApartmentService apartmentService = new ApartmentService(new InMemoryApartmentRepository());

    @Test
    void allotWaterRejectsInvalidRatio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> apartmentService.allotWater("Apt-1", "2", "invalid")
        );

        assertEquals("Water ratio must be in the format 'corporation:borewell' with positive numeric values.", exception.getMessage());
    }

    @Test
    void addGuestsRejectsNonPositiveCount() {
        apartmentService.allotWater("Apt-2", "3", "1:1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> apartmentService.addGuests("Apt-2", 0)
        );

        assertEquals("Guest count must be greater than zero.", exception.getMessage());
    }

    @Test
    void generateBillRequestBuildsExpectedEvent() {
        apartmentService.allotWater("Apt-3", "2", "3:7");
        apartmentService.addGuests("Apt-3", 2);

        BillRequestedEvent event = apartmentService.generateBillRequest("Apt-3");

        assertEquals("Apt-3", event.getApartmentId());
        assertEquals("2", event.getApartmentType());
        assertEquals("3:7", event.getWaterRatio());
        assertEquals(2, event.getTotalGuests());
    }
}
