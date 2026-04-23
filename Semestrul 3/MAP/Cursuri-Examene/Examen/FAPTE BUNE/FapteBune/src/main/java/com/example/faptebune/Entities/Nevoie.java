package com.example.faptebune.Entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Nevoie extends Entity<Long>{

    private String titlu;

    private String descriere;

    private LocalDateTime deadline;

    private long omInNevoie;

    private long omSalvator;

    private String status;

    public Nevoie(String titlu, String descriere, LocalDateTime deadline, long omInNevoie, long omSalvator, String status) {
        this.titlu = titlu;
        this.descriere = descriere;
        this.deadline = deadline;
        this.omInNevoie = omInNevoie;
        this.omSalvator = omSalvator;
        this.status = status;
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

    public long getOmInNevoie() {
        return omInNevoie;
    }

    public void setOmInNevoie(long omInNevoie) {
        this.omInNevoie = omInNevoie;
    }

    public long getOmSalvator() {
        return omSalvator;
    }

    public void setOmSalvator(long omSalvator) {
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
        return omInNevoie == nevoie.omInNevoie && omSalvator == nevoie.omSalvator && Objects.equals(titlu, nevoie.titlu) && Objects.equals(descriere, nevoie.descriere) && Objects.equals(deadline, nevoie.deadline) && Objects.equals(status, nevoie.status);
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
