package com.example.rezerva.domain;

import java.util.Objects;

public class Client{
    private String username;
    private String name;

    public Client(String username, String name) {
        this.username=username;
        this.name=name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return username.equals(client.username) && name.equals(client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name);
    }
}
