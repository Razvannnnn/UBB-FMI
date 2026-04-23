package com.example.ati.Events;


import com.example.ati.Entities.Pat;

public class PatChangeEvent implements Event {
    private ChangeEventType type;
    private Pat data,oldData;

    public PatChangeEvent(ChangeEventType type, Pat data) {
        this.type = type;
        this.data = data;
    }

    public PatChangeEvent(ChangeEventType type, Pat data, Pat oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Pat getData() {
        return data;
    }

    public Pat getOldData() {
        return oldData;
    }
}

