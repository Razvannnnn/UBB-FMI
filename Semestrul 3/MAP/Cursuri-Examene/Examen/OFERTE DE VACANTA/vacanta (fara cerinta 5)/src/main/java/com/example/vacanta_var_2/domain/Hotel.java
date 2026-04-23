package com.example.vacanta_var_2.domain;

import java.util.Objects;

public class Hotel {
    private Double id;
    private Double lid;
    private String name;
    private Integer rooms;
    private Integer price;
    private HotelType type;

    public Hotel(Double id, Double lid, String name, Integer rooms, Integer price, HotelType type) {
        this.id = id;
        this.lid = lid;
        this.name = name;
        this.rooms = rooms;
        this.price = price;
        this.type = type;
    }

    public Double getId() {
        return id;
    }
    public void setId(Double id) {
        this.id = id;
    }
    public Double getLid() {
        return lid;
    }
    public String getName() {
        return name;
    }
    public Integer getRooms() {
        return rooms;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public HotelType getType() {
        return type;
    }

    public static HotelType stringToType(String type){
        if(type.equals("FAMILY"))return HotelType.FAMILY;
        if(type.equals("TEENAGERS"))return HotelType.TEENAGERS;
        if(type.equals("OLDPEOPLE"))return HotelType.OLDPEOPLE;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return id.equals(hotel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
