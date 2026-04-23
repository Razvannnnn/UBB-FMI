package com.example.rezerva.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderItem {
    private Integer idOrder;
    private Integer idItem;
    public OrderItem(Integer idOrder,Integer idItem) {
        this.idOrder=idOrder;
        this.idItem=idItem;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return idOrder.equals(orderItem.idOrder) && idItem.equals(orderItem.idItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrder, idItem);
    }
}
