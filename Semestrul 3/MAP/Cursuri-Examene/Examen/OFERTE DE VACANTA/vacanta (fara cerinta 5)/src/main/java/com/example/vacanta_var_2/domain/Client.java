package com.example.vacanta_var_2.domain;

import java.util.Objects;

public class Client {
    private Long id;
    private String name;
    private Integer fidelityGrade;
    private Integer varsta;
    private Hobbies hobby;

    public Client(Long id, String name, Integer fidelityGrade, Integer varsta, Hobbies hobby) {
        this.id = id;
        this.name = name;
        this.fidelityGrade = fidelityGrade;
        this.varsta = varsta;
        this.hobby = hobby;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getFidelityGrade() {
        return fidelityGrade;
    }

    public Integer getVarsta() {
        return varsta;
    }

    public Hobbies getHobby() {
        return hobby;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFidelityGrade(Integer fidelityGrade) {
        this.fidelityGrade = fidelityGrade;
    }

    public void setVarsta(Integer varsta) {
        this.varsta = varsta;
    }

    public void setHobby(Hobbies hobby) {
        this.hobby = hobby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id) && name.equals(client.name) && fidelityGrade.equals(client.fidelityGrade) && varsta.equals(client.varsta) && hobby == client.hobby;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fidelityGrade, varsta, hobby);
    }
}
