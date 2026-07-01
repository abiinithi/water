package com.example.water.apartment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ApartmentRepositoryIntegrationTest {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Test
    void persistsAndRetrievesApartment() {
        Apartment apartment = new Apartment("Apt-100", "2", "1:2");
        apartmentRepository.save(apartment);

        Optional<Apartment> persisted = apartmentRepository.findById("Apt-100");

        assertTrue(persisted.isPresent());
        assertEquals("Apt-100", persisted.get().getId());
        assertEquals("2", persisted.get().getType());
        assertEquals("1:2", persisted.get().getWaterRatio());
        assertEquals(0, persisted.get().getTotalGuests());
    }
}
