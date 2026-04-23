package com.example.vacanta_var_2.domain;

public enum HotelType {
    FAMILY("FAMILY"), TEENAGERS("TEENAGERS"), OLDPEOPLE("OLDPEOPLE");

    //practic val din paranteza e asociata la cea de dinainte, ca sa pot afce stringuri din enum
    private final String text;
    HotelType(String text){
        this.text=text;
    }

    @Override
    public String toString(){
        return this.text;
    }
}
