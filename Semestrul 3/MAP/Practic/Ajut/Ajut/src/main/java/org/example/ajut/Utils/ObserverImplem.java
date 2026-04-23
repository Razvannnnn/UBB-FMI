package org.example.ajut.Utils;

import java.util.ArrayList;
import java.util.List;

public class ObserverImplem implements Observable{
    List<Observer> observerList = new ArrayList<>();

    @Override
    public void addObserver(Observer e) {
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observerList.remove(e);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer:observerList){
            observer.update();
        }
    }
}
