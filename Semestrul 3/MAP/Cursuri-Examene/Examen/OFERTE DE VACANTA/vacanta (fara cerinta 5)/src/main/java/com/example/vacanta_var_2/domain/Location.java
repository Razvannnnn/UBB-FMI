package com.example.vacanta_var_2.domain;

public class Location {
    private Double id;
    private String name;

    public Location(Double id, String name) {
        this.id = id;
        this.name = name;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
