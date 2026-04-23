package org.example.ajut.Domain;

public class Entity<ID> {

    private Long serialVersionUID = 1L;
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
