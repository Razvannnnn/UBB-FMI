package problema8.network.jsonprotocol;

import problema8.model.Event;
import problema8.network.dto.AgeGroupDTO;
import problema8.network.dto.UserDTO;

public class Request {
    private long Type;
    private UserDTO User;
    private AgeGroupDTO AgeGroup;
    private Long AgeGroupId;
    private String Nume;
    private String Cnp;
    private Event EventName1;
    private Long Id;

    public Request() {}
    public long getType() {
        return Type;
    }
    public void setType(long type) {
        this.Type = type;
    }
    public UserDTO getUser() {
        return User;
    }
    public void setUser(UserDTO user) {
        this.User = user;
    }
    public AgeGroupDTO getAgeGroup() {
        return AgeGroup;
    }
    public void setAgeGroup(AgeGroupDTO ageGroup) {
        this.AgeGroup = ageGroup;
    }
    public Long getAge_group_id() {
        return AgeGroupId;
    }
    public void setAge_group_id(Long age_group_id) {
        this.AgeGroupId = age_group_id;
    }

    public void setNume(String nume) {
        this.Nume = nume;
    }

    public void setCnp(String cnp) {
        this.Cnp = cnp;
    }

    public void setEventName1(Event eventName1) {
        this.EventName1 = eventName1;
    }

    public String getNume() {
        return Nume;
    }

    public String getCnp() {
        return Cnp;
    }

    public Event getEventName1() {
        return EventName1;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }
}
