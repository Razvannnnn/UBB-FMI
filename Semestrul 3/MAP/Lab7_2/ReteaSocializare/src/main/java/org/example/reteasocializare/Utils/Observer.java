package org.example.reteasocializare.Utils;

import org.example.reteasocializare.Utils.Events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
