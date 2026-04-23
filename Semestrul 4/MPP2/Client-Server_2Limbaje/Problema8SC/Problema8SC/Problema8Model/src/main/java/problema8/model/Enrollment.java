package problema8.model;

import java.util.Objects;

public class Enrollment extends Entity<Long> {
    private Child Child;
    private Event Event;

    public Enrollment(Long id, Child child, Event event) {
        setId(id);
        this.Child = child;
        this.Event = event;
    }

    public Child getChild() {
        return Child;
    }

    public void setChild(Child child) {
        this.Child = child;
    }

    public Event getEvent() {
        return Event;
    }

    public void setEvent(Event event) {
        this.Event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(Child, that.Child) && Objects.equals(Event, that.Event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Child, Event);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "child=" + Child +
                ", event=" + Event +
                '}';
    }
}
