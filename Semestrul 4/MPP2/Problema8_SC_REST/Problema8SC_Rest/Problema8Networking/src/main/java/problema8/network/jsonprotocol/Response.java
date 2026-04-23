package problema8.network.jsonprotocol;

import problema8.model.AgeGroup;
import problema8.model.Enrollment;
import problema8.network.dto.ChildDTO;
import problema8.network.dto.EnrollmentDTO;
import problema8.network.dto.EventDTO;
import problema8.network.dto.UserDTO;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private UserDTO user;
    private ChildDTO child;
    private EnrollmentDTO enrollment;
    private EventDTO event;
    private Object data = null;

    public Response() {
    }
    public ResponseType getType() {
        return type;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }
    public EventDTO getEvent() {
        return event;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }
    public ChildDTO getChild() {
        return child;
    }
    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public Object getData() {
        return data;
    }
    public void setEnrollment(EnrollmentDTO enrollment) {
        this.enrollment = enrollment;
    }
    public EnrollmentDTO getEnrollment() {
        return enrollment;
    }

    public void setData(Object newData) {
        this.data = newData;
    }
}
