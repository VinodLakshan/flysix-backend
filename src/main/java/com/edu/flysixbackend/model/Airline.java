package com.edu.flysixbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class Airline implements Serializable {


    @Id
    @Column(length = 10)
    private String code;
    private String name;

    @OneToMany(mappedBy = "airline")
    @JsonIgnore
    private List<Flight> flights;

    public Airline() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String label) {
        this.name = label;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    @Override
    public boolean equals(Object o) {
        Airline otherAirport = (Airline) o;
        return this.code.equalsIgnoreCase(otherAirport.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
