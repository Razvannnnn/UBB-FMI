package com.example.rezerva.domain;

public enum Status {
    PLACED("PLACED"), PREPARING("PREPARING"), SERVED("SERVED");

    //practic val din paranteza e asociata la cea de dinainte, ca sa pot afce stringuri din enum
    private final String text;
    Status(String text){
        this.text=text;
    }

    @Override
    public String toString(){
        return this.text;
    }
}
