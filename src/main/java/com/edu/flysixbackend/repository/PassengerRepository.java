package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
