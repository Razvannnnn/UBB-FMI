package com.example.vacanta_var_2.domain;

public enum Hobbies {
    reading("reading"), music("music"), hiking("hiking"),walking("walking"),extrem_sports("extrem_sports");

    //practic val din paranteza e asociata la cea de dinainte, ca sa pot afce stringuri din enum
    private final String text;
    Hobbies(String text){
        this.text=text;
    }

    @Override
    public String toString(){
        return this.text;
    }
}
