package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
