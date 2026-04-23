package problema8.network.jsonprotocol;

import problema8.model.AgeGroup;
import problema8.model.Enrollment;
import problema8.network.dto.ChildDTO;
import problema8.network.dto.EnrollmentDTO;
import problema8.network.dto.EventDTO;
import problema8.network.dto.UserDTO;

import java.io.Serializable;

public class Response implements Serializable {
    private long Type;
    private String ErrorMessage;
    private UserDTO User;
    private ChildDTO Child;
    private EnrollmentDTO Enrollment;
    private EventDTO Event;
    private Object Data = null;

    public Response() {
    }
    public long getType() {
        return Type;
    }

    public void setEvent(EventDTO event) {
        this.Event = event;
    }
    public EventDTO getEvent() {
        return Event;
    }

    public void setType(long type) {
        this.Type = type;
    }
    public String getErrorMessage() {
        return ErrorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.ErrorMessage = errorMessage;
    }
    public UserDTO getUser() {
        return User;
    }
    public void setUser(UserDTO user) {
        this.User = user;
    }
    public ChildDTO getChild() {
        return Child;
    }
    public void setChild(ChildDTO child) {
        this.Child = child;
    }

    public Object getData() {
        return Data;
    }
    public void setEnrollment(EnrollmentDTO enrollment) {
        this.Enrollment = enrollment;
    }
    public EnrollmentDTO getEnrollment() {
        return Enrollment;
    }

    public void setData(Object newData) {
        this.Data = newData;
    }
}
