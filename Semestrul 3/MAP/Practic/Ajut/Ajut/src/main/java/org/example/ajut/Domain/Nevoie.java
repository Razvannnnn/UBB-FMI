package org.example.ajut.Domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Nevoie extends Entity<Long> {
    private String titlu;
    private String descriere;
    private LocalDateTime deadline;
    private Long omInNevoie;
    private Long omSalvator;
    private String status;

    public Nevoie(String titlu, String descriere, LocalDateTime deadline, Long omInNevoie, Long omSalvator, String status) {
        this.titlu = titlu;
        this.descriere = descriere;
        this.deadline = deadline;
        this.omInNevoie = omInNevoie;
        this.omSalvator = omSalvator;
        this.status = status;
    }

    public Long getId() {
        return super.getId();
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Long getOmInNevoie() {
        return omInNevoie;
    }

    public void setOmInNevoie(Long omInNevoie) {
        this.omInNevoie = omInNevoie;
    }

    public Long getOmSalvator() {
        return omSalvator;
    }

    public void setOmSalvator(Long omSalvator) {
        this.omSalvator = omSalvator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nevoie nevoie = (Nevoie) o;
        return Objects.equals(titlu, nevoie.titlu) && Objects.equals(descriere, nevoie.descriere) && Objects.equals(deadline, nevoie.deadline) && Objects.equals(omInNevoie, nevoie.omInNevoie) && Objects.equals(omSalvator, nevoie.omSalvator) && Objects.equals(status, nevoie.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titlu, descriere, deadline, omInNevoie, omSalvator, status);
    }

    @Override
    public String toString() {
        return "Nevoie{" +
                "titlu='" + titlu + '\'' +
                ", descriere='" + descriere + '\'' +
                ", deadline=" + deadline +
                ", omInNevoie=" + omInNevoie +
                ", omSalvator=" + omSalvator +
                ", status='" + status + '\'' +
                '}';
    }
}
