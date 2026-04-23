package com.example.vacanta_var_2.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Rezervare {
    private Double id;
    private Long idClient;
    private Double idHotel;
    private LocalDate dataStart;
    private Integer noNights;

    public Rezervare(Double id, Long idClient,Double idHotel,LocalDate dataStart,Integer noNights) {
        this.id = id;
        this.idClient=idClient;
        this.idHotel=idHotel;
        this.dataStart=dataStart;
        this.noNights=noNights;
    }

    public Double getId() {
        return id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public Double getIdHotel() {
        return idHotel;
    }

    public LocalDate getDataStart() {
        return dataStart;
    }

    public Integer getNoNights() {
        return noNights;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public void setIdHotel(Double idHotel) {
        this.idHotel = idHotel;
    }

    public void setDataStart(LocalDate dataStart) {
        this.dataStart = dataStart;
    }

    public void setNoNights(Integer noNights) {
        this.noNights = noNights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rezervare rezervare = (Rezervare) o;
        return id.equals(rezervare.id) && idClient.equals(rezervare.idClient) && idHotel.equals(rezervare.idHotel) && dataStart.equals(rezervare.dataStart) && noNights.equals(rezervare.noNights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idClient, idHotel, dataStart, noNights);
    }
}
