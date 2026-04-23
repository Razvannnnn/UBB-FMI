package com.example.anar.observer;


import java.util.HashSet;
import java.util.Set;

public abstract class ConcreteObservable implements Observable {
    private Set<Observer> observers = new HashSet<>();

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
        observers.forEach(Observer::update);
    }
}