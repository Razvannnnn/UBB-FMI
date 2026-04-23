package com.example.anar.domain;

public class Localitate {
    private int id;
    private String nume;
    private int rauId;
    private int cmdr;
    private int cma;

    public Localitate(int id, String nume, int rauId, int cmdr, int cma) {
        this.id = id;
        this.nume = nume;
        this.rauId = rauId;
        this.cmdr = cmdr;
        this.cma = cma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getRauId() {
        return rauId;
    }

    public void setRauId(int rauId) {
        this.rauId = rauId;
    }

    public int getCmdr() {
        return cmdr;
    }

    public void setCmdr(int cmdr) {
        this.cmdr = cmdr;
    }

    public int getCma() {
        return cma;
    }

    public void setCma(int cma) {
        this.cma = cma;
    }
}
