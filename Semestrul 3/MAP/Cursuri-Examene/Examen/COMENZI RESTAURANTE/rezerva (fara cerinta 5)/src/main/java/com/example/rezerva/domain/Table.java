package com.example.rezerva.domain;

import java.util.Objects;

public class Table{
    private Integer id;

    public Table(Integer id) {
        this.id=id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id.equals(table.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
