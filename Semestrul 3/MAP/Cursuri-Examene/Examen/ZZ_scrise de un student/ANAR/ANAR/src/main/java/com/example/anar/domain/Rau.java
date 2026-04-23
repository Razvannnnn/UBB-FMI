package com.example.anar.domain;

public class Rau {
    private int id;
    private String nume;
    private int cota;

    public Rau(int id, String nume, int cota) {
        this.id = id;
        this.nume = nume;
        this.cota = cota;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getCota() {
        return cota;
    }

    public void setCota(int cota) {
        this.cota = cota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

