package org.example.reteasocializare.Domain;

import java.text.Format;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.stream.LongStream;

public class Prietenie extends Entity<Long> {

    LocalDateTime date;

    String numeUser1;
    String numeUser2;

    Long idUser1;
    Long idUser2;
    Integer status;

    public Prietenie(Long idUser1, Long idUser2, Integer status) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.status = status;
        this.date = LocalDateTime.now();
    }

    public Long getIdUtilizator1() {
        return idUser1;
    }

    public Long getIdUtilizator2() {
        return idUser2;
    }

    public Integer getStatus() {
        return status; }

    public Long getId() {
        return super.getId();
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getNumeUser1() {
        return numeUser1;
    }

    public String getNumeUser2() {
        return numeUser2;
    }

    public void setNumeUser1(String numeUser1) {
        this.numeUser1 = numeUser1;
    }

    public void setNumeUser2(String numeUser2) {
        this.numeUser2 = numeUser2;
    }

    @Override
    public String toString() {
        return this.numeUser2 +
                " Prieteni din: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
