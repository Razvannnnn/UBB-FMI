package com.example.ati.Observer;

import com.example.ati.Events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E entity);
}
