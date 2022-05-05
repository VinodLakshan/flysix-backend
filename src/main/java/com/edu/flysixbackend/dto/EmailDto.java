package com.edu.flysixbackend.dto;

public class EmailDto {

    private long bookingId;
    private String bookingClass;
    private String trip;
    private String reservedBy;
    private String departAt;
    private String currency;
    private double total;
    private String to;

    public EmailDto() {
    }

    public EmailDto(long bookingId, String bookingClass, String trip, String reservedBy, String departAt, String currency, double total, String to) {
        this.bookingId = bookingId;
        this.bookingClass = bookingClass;
        this.trip = trip;
        this.reservedBy = reservedBy;
        this.departAt = departAt;
        this.currency = currency;
        this.total = total;
        this.to = to;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }

    public String getDepartAt() {
        return departAt;
    }

    public void setDepartAt(String departAt) {
        this.departAt = departAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
