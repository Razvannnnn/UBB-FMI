package com.example.clinicaprivata.Observer;

import com.example.clinicaprivata.Events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}