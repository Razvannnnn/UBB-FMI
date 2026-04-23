package problema8.network.dto;

import java.io.Serializable;

public class EventDTO implements Serializable {
    private String name;
    private Integer distance;
    private Long ageGroupId;

    public EventDTO(String name, Integer distance, Long ageGroupId) {
        this.name = name;
        this.distance = distance;
        this.ageGroupId = ageGroupId;
    }

    public String getName() {
        return name;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getAgeGroupId() {
        return ageGroupId;
    }

    @Override
    public String toString() {
        return "EventDTO[" + name + " " + distance + " " + ageGroupId + "]";
    }
}
