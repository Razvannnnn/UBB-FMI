package ro.mpp2025.domain;

import java.util.ArrayList;
import java.util.List;

public class Enrollment implements Entity<Integer> {
    private Integer id;
    private final Child child;
    private final Event event;

    public Enrollment(Child child, Event event) {
        this.child = child;
        this.event = event;
    }

    public Child getChild() {
        return child;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + "|" + child + "|" + event;
    }
}
