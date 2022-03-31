package com.edu.flysixbackend.service;

import com.edu.flysixbackend.dto.BookingDto;
import com.edu.flysixbackend.model.Booking;

public interface BookingService {

    Booking saveBooking(BookingDto bookingDto);
    Booking findBooking(Long bookingId);
}
