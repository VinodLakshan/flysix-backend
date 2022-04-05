package com.edu.flysixbackend.service.impl;

import com.edu.flysixbackend.constants.Const;
import com.edu.flysixbackend.dto.BookingDto;
import com.edu.flysixbackend.dto.EmailDto;
import com.edu.flysixbackend.exception.AlreadyDoneException;
import com.edu.flysixbackend.exception.ExpiredException;
import com.edu.flysixbackend.model.*;
import com.edu.flysixbackend.repository.AirlineRepository;
import com.edu.flysixbackend.repository.AirportRepository;
import com.edu.flysixbackend.repository.BookingRepository;
import com.edu.flysixbackend.service.BookingService;
import com.edu.flysixbackend.service.EmailService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CancellationException;
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
    private EmailService emailService;

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
        }

        return null;
    }

    @Override
    public Booking findBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Override
    public boolean confirmBooking(Long bookingId) {

        log.info("Booking Id " + bookingId + " is being confirmed.");
        Booking booking = findBooking(bookingId);

        if (booking != null) {
            booking.getPayment().setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            booking.getPayment().setStatus(Const.PAYMENT_COMPLETED);
            booking.setStatus(Const.BOOKING_CONFIRMED);

            bookingRepository.save(booking);
            log.info("Booking Id " + bookingId + " confirmed.");

            this.sendMail(booking);
            return true;

        } else {
            return false;
        }
    }

    @Override
    public Booking findUnconfirmedOnHoldBooking(Long bookingId) throws Exception {
        Booking booking = bookingRepository
                .findByBookingIdAndBookingTypeEquals(bookingId, Const.BOOKING_TYPE_HOLD_FOR_FREE);

        if (booking != null) {

            if (booking.getStatus().equalsIgnoreCase(Const.BOOKING_UNCONFIRMED)) {
                return booking;

            } else if (booking.getStatus().equalsIgnoreCase(Const.BOOKING_CANCELLED)) {
                throw new CancellationException("This booking is cancelled.");

            } else if (booking.getStatus().equalsIgnoreCase(Const.BOOKING_EXPIRED)) {
                throw new ExpiredException("This booking is cancelled due to expiration of on hold period.");

            } else {
                throw new AlreadyDoneException("This booking is already Confirmed.");
            }

        } else {
            throw new NotFoundException("Booking not found.");
        }
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

    private void sendMail(Booking booking) {

        EmailDto confirmEmail = new EmailDto();
        confirmEmail.setBookingId(booking.getBookingId());
        confirmEmail.setBookingClass(booking.getBookingClass());
        confirmEmail.setTrip(booking.getTrip());
        confirmEmail.setReservedBy(booking.getReservedBy().getName());
        confirmEmail.setDepartAt(booking.getFlight().getDepartTrip().getDepartAt());
        confirmEmail.setCurrency(booking.getPayment().getCurrency());
        confirmEmail.setTotal(booking.getPayment().getTotal());
        confirmEmail.setTo(booking.getPassengers().get(0).getEmail());

        emailService.sendConfirmationMail(confirmEmail);
    }
}
