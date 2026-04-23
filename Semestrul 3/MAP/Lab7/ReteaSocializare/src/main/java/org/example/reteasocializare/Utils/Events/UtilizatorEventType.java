package org.example.reteasocializare.Utils.Events;

import org.example.reteasocializare.Domain.Utilizator;

public class UtilizatorEventType extends Event {
    private EventType type;
    private Utilizator data, oldData;

    public UtilizatorEventType(EventType type, Utilizator data) {
        this.type = type;
        this.data = data;
    }

    public UtilizatorEventType(EventType type, Utilizator data, Utilizator oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public EventType getType() {
        return type;
    }

    public Utilizator getData() {
        return data;
    }

    public Utilizator getOldData() {
        return oldData;
    }
}
