package ro.mpp2025.problema8_fx.Domain;

import java.util.Objects;

public class Entity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}