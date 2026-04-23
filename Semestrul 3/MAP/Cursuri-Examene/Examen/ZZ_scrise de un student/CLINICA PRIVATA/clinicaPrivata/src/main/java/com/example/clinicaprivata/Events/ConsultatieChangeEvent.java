package com.example.clinicaprivata.Events;

import com.example.clinicaprivata.Entities.Consultatie;

public class ConsultatieChangeEvent implements Event {
    private ChangeEventType type;
    private Consultatie data,oldData;

    public ConsultatieChangeEvent(ChangeEventType type, Consultatie data) {
        this.type = type;
        this.data = data;
    }

    public ConsultatieChangeEvent(ChangeEventType type, Consultatie data, Consultatie oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Consultatie getData() {
        return data;
    }

    public Consultatie getOldData() {
        return oldData;
    }
}