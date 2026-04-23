package org.example.comenzi2.Utils;

import java.util.ArrayList;
import java.util.List;

public class ObserverImplem implements Observable{
    List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer:observers){
            observer.update();
        }
    }

}
