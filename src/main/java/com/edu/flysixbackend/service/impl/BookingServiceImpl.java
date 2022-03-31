package com.edu.flysixbackend.service.impl;

import com.edu.flysixbackend.constants.Const;
import com.edu.flysixbackend.dto.BookingDto;
import com.edu.flysixbackend.model.*;
import com.edu.flysixbackend.repository.AirlineRepository;
import com.edu.flysixbackend.repository.AirportRepository;
import com.edu.flysixbackend.repository.BookingRepository;
import com.edu.flysixbackend.repository.FlightRepository;
import com.edu.flysixbackend.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public Booking saveBooking(BookingDto bookingDto) {

        Booking booking = new Booking(bookingDto);

        booking.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date()));

        Notification notification = new Notification(bookingDto.getPassengers().get(0));
        booking.setNotification(notification);

        booking.getPayment().setStatus(Const.PAYMENT_PENDING);

        if (!airlineRepository.existsById(booking.getFlight().getAirline().getCode())) {
            airlineRepository.save(booking.getFlight().getAirline());
        }
        boolean saved = this.saveAirports(booking.getFlight());

        if (saved) {
            Booking savedBooking = bookingRepository.save(booking);
            return savedBooking;
        };
        return null;
    }

    @Override
    public Booking findBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
//        Flight booking = flightRepository.findById(bookingId).orElse(null);
        return booking;
    }

    private boolean saveAirports(Flight flight) {

        List<Segment> allSegments = Stream.concat(flight.getDepartTrip().getSegments().stream(),
                        flight.getReturnTrip().getSegments().stream())
                .collect(Collectors.toList());

        Set<Airport> airports = new HashSet<>();

        boolean isSaved = true;

        for (Segment segment : allSegments) {
            airports.add(segment.getOrigin());
            airports.add(segment.getDestination());
        }

        for (Airport airport : airports) {

            if (!airportRepository.existsById(airport.getId())) {
                Airport save = airportRepository.save(airport);
                if (save == null) isSaved = false;
            }
        }
        return isSaved;
    }
}
