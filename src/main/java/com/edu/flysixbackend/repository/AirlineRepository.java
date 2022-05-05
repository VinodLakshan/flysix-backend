package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, String> {

    @Override
    boolean existsById(String s);
}
