package com.example.rezerva.domain;

import java.util.Objects;

public class MenuItem {
    private Integer id;
    private String category;
    private String item;
    private Float price;
    private String currency;

    public MenuItem(Integer id,String category,String item,Float price,String currency) {
        this.id=id;
        this.category=category;
        this.item=item;
        this.price=price;
        this.currency=currency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return id.equals(menuItem.id) && category.equals(menuItem.category) && item.equals(menuItem.item) && price.equals(menuItem.price) && currency.equals(menuItem.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, item, price, currency);
    }
}
