package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByBookingIdAndBookingTypeEquals(Long bookingId, String bookingType);
    List<Booking> findByReservedBy_UserId(Long userId);
}
