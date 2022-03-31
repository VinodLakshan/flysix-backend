package com.edu.flysixbackend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Flight implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @ManyToOne
    private Airline airline;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Itinerary departTrip;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Itinerary returnTrip;

    public Flight() {
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public Itinerary getDepartTrip() {
        return departTrip;
    }

    public void setDepartTrip(Itinerary departTrip) {
        this.departTrip = departTrip;
    }

    public Itinerary getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(Itinerary returnTrip) {
        this.returnTrip = returnTrip;
    }

}
