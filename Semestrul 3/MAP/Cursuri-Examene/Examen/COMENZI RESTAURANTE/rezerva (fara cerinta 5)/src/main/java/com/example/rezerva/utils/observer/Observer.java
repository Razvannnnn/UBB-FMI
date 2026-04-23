package com.example.rezerva.utils.observer;

import com.example.rezerva.utils.events.Event;

public interface Observer <E extends Event>{
    void update(E e);
}
