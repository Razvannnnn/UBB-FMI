package com.example.rezerva.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {
    private Integer id;
    private Integer idTable;
    private LocalDateTime data;
    private Status status;
    public Order(Integer id,Integer idTable, LocalDateTime data,Status status) {
        this.id=id;
        this.idTable=idTable;
        this.data=data;
        this.status=status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTable() {
        return idTable;
    }

    public void setIdTable(Integer idTable) {
        this.idTable = idTable;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id) && idTable.equals(order.idTable) && data.equals(order.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idTable, data);
    }
}
