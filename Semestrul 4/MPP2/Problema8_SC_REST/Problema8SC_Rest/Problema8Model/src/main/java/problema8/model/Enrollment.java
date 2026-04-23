package problema8.model;

import java.util.Objects;

public class Enrollment extends Entity<Long> {
    private Child child;
    private Event event;

    public Enrollment(Long id, Child child, Event event) {
        setId(id);
        this.child = child;
        this.event = event;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(child, that.child) && Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, event);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "child=" + child +
                ", event=" + event +
                '}';
    }
}
