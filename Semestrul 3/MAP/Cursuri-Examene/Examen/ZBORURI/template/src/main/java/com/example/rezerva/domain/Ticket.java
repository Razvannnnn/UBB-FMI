package com.example.rezerva.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {
    private String username;
    private Long idFlight;
    private LocalDateTime data;

    public Ticket(String username, Long idFlight,LocalDateTime data) {
        this.username=username;
        this.idFlight=idFlight;
        this.data=data;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(Long idFlight) {
        this.idFlight = idFlight;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return username.equals(ticket.username) && idFlight.equals(ticket.idFlight) && data.equals(ticket.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, idFlight, data);
    }
}
