package com.example.water.apartment;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryApartmentRepository implements ApartmentRepository {
    private final Map<String, Apartment> apartmentStorage = new ConcurrentHashMap<>();

    @Override
    public Optional<Apartment> findById(String id) {
        return Optional.ofNullable(apartmentStorage.get(id));
    }

    @Override
    public void save(Apartment apartment) {
        apartmentStorage.put(apartment.getId(), apartment);
    }
}
