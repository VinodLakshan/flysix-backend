package com.edu.flysixbackend.controller;

import com.edu.flysixbackend.dto.BookingDto;
import com.edu.flysixbackend.dto.ErrorDto;
import com.edu.flysixbackend.exception.AlreadyDoneException;
import com.edu.flysixbackend.exception.ExpiredException;
import com.edu.flysixbackend.model.Booking;
import com.edu.flysixbackend.service.BookingService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CancellationException;

@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "*")
@Slf4j
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/unconfirmedOnHolds/{bookingId}")
    public ResponseEntity<?> searchUnconfirmedOnHolds(@PathVariable long bookingId) {
        log.info("retrieving the booking...");

        try {
            Booking bookingById = bookingService.findUnconfirmedOnHoldBooking(bookingId);
            return ResponseEntity.ok(bookingById);

        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);

        } catch (AlreadyDoneException e) {

            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);

        } catch (CancellationException e) {

            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);

        } catch (ExpiredException e) {

            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);

        } catch (Exception e) {

            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> searchBooking(@PathVariable long bookingId) {
        log.info("retrieving the booking...");

        try {
            Booking bookingById = bookingService.findBooking(bookingId);

            if (bookingById != null) {
                log.info("Booking found");
                return ResponseEntity.ok(bookingById);

            } else {
                log.error("Booking not found");
                return new ResponseEntity<>(new ErrorDto("Booking Id is incorrect.", HttpStatus.INTERNAL_SERVER_ERROR),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<?> saveReservation(@RequestBody BookingDto reservationDetails) {
        log.info("Saving reservation...");
        try {
            Booking booking = bookingService.saveBooking(reservationDetails);

            if (booking != null) {
                log.info("Booking saved.");
                return ResponseEntity.ok(booking);

            } else {
                return new ResponseEntity<>(new ErrorDto("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("Booking failed.");
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateReservation(@RequestBody BookingDto reservationDetails) {
        log.info("Saving reservation...");
        return ResponseEntity.ok("response");
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<?> getBookingsForUser(@PathVariable Long userId) {

        try {
            return ResponseEntity.ok(bookingService.getBookingForUser(userId));

        } catch (Exception e) {
            log.error("Searching for bookings by user failed.");
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
