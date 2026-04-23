package org.example.reteasocializare.Utils.Events;

import org.example.reteasocializare.Domain.Utilizator;

public class UtilizatorEvent implements Event {
    private ChangeEventType type;
    private Utilizator oldData, data;

    public UtilizatorEvent(ChangeEventType type, Utilizator data) {
        this.type = type;
        this.data = data;
    }

    public UtilizatorEvent(ChangeEventType type, Utilizator oldData, Utilizator data) {
        this.type = type;
        this.oldData = oldData;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getOldData() {
        return oldData;
    }

    public Utilizator getData() {
        return data;
    }

}
