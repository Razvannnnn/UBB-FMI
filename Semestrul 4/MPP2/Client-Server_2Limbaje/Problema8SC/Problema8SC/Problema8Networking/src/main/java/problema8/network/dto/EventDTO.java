package problema8.network.dto;

import java.io.Serializable;

public class EventDTO implements Serializable {
    private String Name;
    private Integer Distance;
    private Long AgeGroupId;

    public EventDTO(String name, Integer distance, Long ageGroupId) {
        this.Name = name;
        this.Distance = distance;
        this.AgeGroupId = ageGroupId;
    }

    public String getName() {
        return Name;
    }

    public Integer getDistance() {
        return Distance;
    }

    public Long getAgeGroupId() {
        return AgeGroupId;
    }

    @Override
    public String toString() {
        return "EventDTO[" + Name + " " + Distance + " " + AgeGroupId + "]";
    }
}
