package problema8.network.dto;

import problema8.model.Child;
import problema8.model.Enrollment;
import problema8.model.Event;

import java.io.Serializable;

public class EnrollmentDTO implements Serializable {
    private Long childId;
    private Long eventId;

    public EnrollmentDTO(Long childId, Long eventId) {
        this.childId = childId;
        this.eventId = eventId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    @Override
    public String toString() {
        return "EnrollmentDTO[" + childId + " " + eventId + "]";
    }
}
