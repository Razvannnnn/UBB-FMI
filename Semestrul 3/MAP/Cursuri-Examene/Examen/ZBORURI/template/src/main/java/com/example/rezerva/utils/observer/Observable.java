package com.example.rezerva.utils.observer;

import com.example.rezerva.utils.events.Event;

public interface Observable{
    void addObserver(Observer e);

    void removeObserver(Observer e);

    void notifyObservers();
}
