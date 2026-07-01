package com.example.water.apartment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataApartmentRepository extends JpaRepository<Apartment, String> {
}
