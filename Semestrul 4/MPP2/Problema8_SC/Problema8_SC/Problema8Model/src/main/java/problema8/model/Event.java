package problema8.model;

import java.util.Objects;

public class Event extends Entity<Long> {
    private String name;
    private Integer distance;
    private Long ageGroupId;

    public Event(Long id, String name, Integer distance, Long ageGroupId) {
        setId(id);
        this.name = name;
        this.distance = distance;
        this.ageGroupId = ageGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Long getAgeGroupId() {
        return ageGroupId;
    }

    public void setAgeGroupId(Long ageGroupId) {
        this.ageGroupId = ageGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(distance, event.distance) && Objects.equals(ageGroupId, event.ageGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, distance, ageGroupId);
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", distance=" + distance +
                ", ageGroupId=" + ageGroupId +
                '}';
    }
}
