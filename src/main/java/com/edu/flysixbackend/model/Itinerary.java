package com.edu.flysixbackend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Itinerary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itineraryId;
    private String duration;
    private String departAt;
    private String arriveAt;

    @ManyToOne
    private Airport origin;

    @ManyToOne
    private Airport destination;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Segment> segments;

    public Itinerary() {
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDepartAt() {
        return departAt;
    }

    public void setDepartAt(String departAt) {
        this.departAt = departAt;
    }

    public String getArriveAt() {
        return arriveAt;
    }

    public void setArriveAt(String arriveAt) {
        this.arriveAt = arriveAt;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
