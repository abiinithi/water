package com.example.water.apartment;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaApartmentRepository implements ApartmentRepository {

    private final SpringDataApartmentRepository repository;

    public JpaApartmentRepository(SpringDataApartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Apartment> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void save(Apartment apartment) {
        repository.save(apartment);
    }
}
