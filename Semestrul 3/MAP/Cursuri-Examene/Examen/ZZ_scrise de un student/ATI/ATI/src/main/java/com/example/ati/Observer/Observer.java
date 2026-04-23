package com.example.ati.Observer;

import com.example.ati.Events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
