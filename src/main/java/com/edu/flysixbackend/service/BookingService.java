package com.edu.flysixbackend.service;

import com.edu.flysixbackend.dto.BookingDto;
import com.edu.flysixbackend.model.Booking;

import java.util.List;

public interface BookingService {

    Booking saveBooking(BookingDto bookingDto) throws Exception;
    Booking findBooking(Long bookingId) throws Exception;
    boolean confirmBooking(Long bookingId) throws Exception;
    Booking findUnconfirmedOnHoldBooking(Long bookingId) throws Exception;
    List<Booking> getBookingForUser(Long userId) throws Exception;
}
