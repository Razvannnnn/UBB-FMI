package org.example.reteasocializare.Utils;

import org.example.reteasocializare.Utils.Events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
