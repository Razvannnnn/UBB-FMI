package com.example.rezerva.domain;

import java.util.Objects;

public class City {
    private String id;
    private String nume;

    public City(String id, String nume) {
        this.id=id;
        this.nume=nume;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id.equals(city.id) && nume.equals(city.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nume);
    }
}
