package com.edu.flysixbackend.controller;

import com.edu.flysixbackend.dto.BookingDto;
import com.edu.flysixbackend.dto.ErrorDto;
import com.edu.flysixbackend.model.Booking;
import com.edu.flysixbackend.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "*")
@Slf4j
public class BookingController {

    @Autowired
    private BookingService bookingService;

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


}
