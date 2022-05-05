package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository  extends JpaRepository<Airport, String> {

    @Override
    boolean existsById(String s);
}
