package com.edu.flysixbackend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Airport implements Serializable {

    @Id
    @Column(length = 10)
    private String id;
    private String label;

    public Airport() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object obj) {
        Airport otherAirport = (Airport) obj;
        return this.id.equalsIgnoreCase(otherAirport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
