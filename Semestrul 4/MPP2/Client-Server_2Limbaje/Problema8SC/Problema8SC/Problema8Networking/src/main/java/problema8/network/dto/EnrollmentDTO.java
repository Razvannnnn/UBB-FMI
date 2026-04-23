package problema8.network.dto;

import java.io.Serializable;

public class EnrollmentDTO implements Serializable {
    private Long ChildId;
    private Long EventId;

    public EnrollmentDTO(Long childId, Long eventId) {
        this.ChildId = childId;
        this.EventId = eventId;
    }

    public Long getChildId() {
        return ChildId;
    }

    public void setChildId(Long childId) {
        this.ChildId = childId;
    }

    @Override
    public String toString() {
        return "EnrollmentDTO[" + ChildId + " " + EventId + "]";
    }
}
