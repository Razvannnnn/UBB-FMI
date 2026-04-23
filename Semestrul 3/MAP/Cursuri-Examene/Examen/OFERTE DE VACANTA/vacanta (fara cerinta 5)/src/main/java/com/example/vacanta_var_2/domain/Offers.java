package com.example.vacanta_var_2.domain;

import java.time.LocalDate;

public class Offers {
    private Double id;
    private Double hotel;
    private LocalDate start;
    private LocalDate end;
    private Integer percent;

    public Offers(Double id, Double hotel, LocalDate start, LocalDate end, Integer percent) {
        this.id = id;
        this.hotel = hotel;
        this.start = start;
        this.end = end;
        this.percent = percent;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public Double getHotel() {
        return hotel;
    }



    public LocalDate getStart() {
        return start;
    }



    public LocalDate getEnd() {
        return end;
    }



    public Integer getPercent() {
        return percent;
    }
}
