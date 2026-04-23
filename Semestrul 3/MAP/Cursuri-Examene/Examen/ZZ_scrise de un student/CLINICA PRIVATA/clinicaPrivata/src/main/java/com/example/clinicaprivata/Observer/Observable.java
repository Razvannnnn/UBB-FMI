package com.example.clinicaprivata.Observer;

import com.example.clinicaprivata.Events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);

    void removeObserver(Observer<E> e);

    void notifyObservers(E t);
}

