package problema8.model;

import java.util.Objects;

public class Event extends Entity<Long> {
    private String Name;
    private Integer Distance;
    private Long AgeGroupId;

    public Event(Long id, String name, Integer distance, Long ageGroupId) {
        setId(id);
        this.Name = name;
        this.Distance = distance;
        this.AgeGroupId = ageGroupId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Integer getDistance() {
        return Distance;
    }

    public void setDistance(Integer distance) {
        this.Distance = distance;
    }

    public Long getAgeGroupId() {
        return AgeGroupId;
    }

    public void setAgeGroupId(Long ageGroupId) {
        this.AgeGroupId = ageGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(Name, event.Name) && Objects.equals(Distance, event.Distance) && Objects.equals(AgeGroupId, event.AgeGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Distance, AgeGroupId);
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + Name + '\'' +
                ", distance=" + Distance +
                ", ageGroupId=" + AgeGroupId +
                '}';
    }
}
