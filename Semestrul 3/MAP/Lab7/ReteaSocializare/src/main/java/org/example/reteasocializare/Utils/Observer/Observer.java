package org.example.reteasocializare.Utils.Observer;

import org.example.reteasocializare.Utils.Events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
