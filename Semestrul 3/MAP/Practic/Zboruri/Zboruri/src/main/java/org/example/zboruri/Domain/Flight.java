package org.example.zboruri.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight extends Entity<Long>{
    private String from;
    private String to;
    private LocalDateTime departureTime;
    private LocalDateTime landingTime;
    private Integer seats;

    public Flight(Long id, String from, String to, LocalDateTime departureTime, LocalDateTime landingTime, Integer seats) {
        setId(id);
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.seats = seats;
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

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(LocalDateTime landingTime) {
        this.landingTime = landingTime;
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
        return Objects.equals(from, flight.from) && Objects.equals(to, flight.to) && Objects.equals(departureTime, flight.departureTime) && Objects.equals(landingTime, flight.landingTime) && Objects.equals(seats, flight.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, departureTime, landingTime, seats);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", departureTime=" + departureTime +
                ", landingTime=" + landingTime +
                ", seats=" + seats +
                '}';
    }
}
