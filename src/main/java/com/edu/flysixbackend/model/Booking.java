package com.edu.flysixbackend.model;

import com.edu.flysixbackend.dto.BookingDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private String createdDate;
    private String trip;
    private String bookingClass;
    private String bookingType;
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    private Notification notification;

    @ManyToOne
    private User reservedBy;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Passenger> passengers;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Flight flight;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Payment payment;

    public Booking() {
    }

    public Booking(BookingDto bookingDto) {
        this.bookingId = bookingDto.getBookingId();
        this.trip = bookingDto.getTrip();
        this.bookingClass = bookingDto.getBookingClass();
        this.bookingType = bookingDto.getBookingType();
        this.status = bookingDto.getStatus();
        this.reservedBy = bookingDto.getReservedBy();
        this.passengers = bookingDto.getPassengers();
        this.flight = bookingDto.getFlight();
        this.payment = bookingDto.getPayment();
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(User reservedBy) {
        this.reservedBy = reservedBy;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
