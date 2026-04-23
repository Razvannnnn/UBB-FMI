package com.example.rezerva.domain;

import java.util.Objects;

public class TrainStation {
    private String id;
    private String departure;
    private String destination;

    public TrainStation(String id, String departure,String destination) {
        this.id=id;
        this.departure=departure;
        this.destination=destination;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainStation that = (TrainStation) o;
        return id.equals(that.id) && departure.equals(that.departure) && destination.equals(that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departure, destination);
    }
}
