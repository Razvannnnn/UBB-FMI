package org.example.reteasocializare.Domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilizatorCuData {
    private Utilizator utilizator;
    private LocalDateTime date;

    public UtilizatorCuData(Utilizator utilizator, LocalDateTime date) {
        this.utilizator = utilizator;
        this.date = date;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getNume() {
        return utilizator.getNume();
    }

    public String getPrenume() {
        return utilizator.getPrenume();
    }

    public Long getId() {
        return utilizator.getId();
    }

    @Override
    public String toString() {
        return utilizator.getNume() + " " + utilizator.getPrenume() +
                ", prieteni din: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
