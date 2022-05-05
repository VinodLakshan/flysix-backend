package com.edu.flysixbackend.services;

import com.edu.flysixbackend.constants.Const;
import com.edu.flysixbackend.dto.BookingDto;
import com.edu.flysixbackend.dto.EmailDto;
import com.edu.flysixbackend.model.*;
import com.edu.flysixbackend.repository.AirlineRepository;
import com.edu.flysixbackend.repository.BookingRepository;
import com.edu.flysixbackend.service.EmailService;
import com.edu.flysixbackend.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private AirlineRepository airlineRepositoryMock;

    @Mock
    private EmailService emailServiceMock;

    @InjectMocks
    private BookingServiceImpl bookingServiceMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Save Booking")
    public void saveBookingTest() {

        BookingDto bookingDto = this.getBookingDto();
        Booking booking = new Booking(bookingDto);

        Mockito.when(airlineRepositoryMock.existsById(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(bookingRepositoryMock.save(ArgumentMatchers.any(Booking.class))).thenReturn(booking);

        try {
            Assertions.assertNotNull(bookingServiceMock.saveBooking(bookingDto));

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("Test Find Booking")
    public void findBookingTest(){

        Booking booking = new Booking(this.getBookingDto());
        Mockito.when(bookingRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(booking));

        try {
            Assertions.assertNotNull(bookingServiceMock.findBooking(1L));

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("Test Confirm Booking")
    public void confirmBookingTest(){

        Booking booking = new Booking(this.getBookingDto());
        Mockito.when(bookingRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepositoryMock.save(ArgumentMatchers.any(Booking.class))).thenReturn(booking);
        Mockito.when(emailServiceMock.sendConfirmationMail(ArgumentMatchers.any(EmailDto.class))).thenReturn(true);

        try {
            Assertions.assertEquals(true, bookingServiceMock.confirmBooking(1L));

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    private BookingDto getBookingDto() {

        User reservedBy = new User();
        reservedBy.setName("Vinod");

        Itinerary itinerary = new Itinerary();
        itinerary.setDepartAt("2022-05-14 12:00");
        itinerary.setSegments(new ArrayList<>());

        Airline airline = new Airline();
        airline.setCode("CMB");

        Flight flight= new Flight();
        flight.setDepartTrip(itinerary);
        flight.setReturnTrip(itinerary);

        flight.setAirline(airline);

        Payment payment = new Payment();
        payment.setCurrency("USD");
        payment.setTotal(1000);

        List<Passenger> passengers = new ArrayList<>();
        Passenger passenger = new Passenger();
        passenger.setEmail("test@test.com");
        passengers.add(passenger);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingId(1L);
        bookingDto.setReservedBy(reservedBy);
        bookingDto.setBookingType(Const.BOOKING_TYPE_INSTANT);
        bookingDto.setBookingClass(Const.BOOKING_CLASS_ECONOMY);
        bookingDto.setTrip(Const.TRIP_TYPE_ROUND);
        bookingDto.setFlight(flight);
        bookingDto.setPayment(payment);
        bookingDto.setPassengers(passengers);
        bookingDto.setStatus(Const.PAYMENT_PENDING);

        return bookingDto;
    }

}
