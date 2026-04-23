package org.example.zboruri.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket extends Entity<Long>{
    private String username;
    private Long flightId;
    private LocalDateTime purchaseTime;

    public Ticket(Long id, String username, Long flightId, LocalDateTime purchaseTime) {
        setId(id);
        this.username = username;
        this.flightId = flightId;
        this.purchaseTime = purchaseTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(username, ticket.username) && Objects.equals(flightId, ticket.flightId) && Objects.equals(purchaseTime, ticket.purchaseTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, flightId, purchaseTime);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "username='" + username + '\'' +
                ", flightId=" + flightId +
                ", purchaseTime=" + purchaseTime +
                '}';
    }
}
