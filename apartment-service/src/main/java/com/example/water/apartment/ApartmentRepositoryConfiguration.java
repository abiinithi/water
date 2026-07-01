package com.example.water.apartment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApartmentRepositoryConfiguration {

    @Bean
    public ApartmentRepository apartmentRepository(SpringDataApartmentRepository repository) {
        return new JpaApartmentRepository(repository);
    }
}
