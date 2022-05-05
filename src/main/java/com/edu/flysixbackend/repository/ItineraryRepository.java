package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
}
