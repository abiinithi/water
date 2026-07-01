package com.example.water.apartment;

import java.util.Optional;

public interface ApartmentRepository {
    Optional<Apartment> findById(String id);

    void save(Apartment apartment);
}
