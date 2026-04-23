package com.example.rezerva.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight {
    private Long id;
    private String from;
    private String to;
    private LocalDateTime departure;
    private LocalDateTime landing;
    private Integer seats;

    public Flight(Long id, String from,String to,LocalDateTime departure, LocalDateTime landing,Integer seats) {
        this.id=id;
        this.from=from;
        this.to=to;
        this.departure=departure;
        this.landing=landing;
        this.seats=seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public LocalDateTime getLanding() {
        return landing;
    }

    public void setLanding(LocalDateTime landing) {
        this.landing = landing;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return id.equals(flight.id) && from.equals(flight.from) && to.equals(flight.to) && departure.equals(flight.departure) && landing.equals(flight.landing) && seats.equals(flight.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, departure, landing, seats);
    }
}
