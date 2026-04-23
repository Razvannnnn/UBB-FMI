package problema8.network.jsonprotocol;

import problema8.model.Event;
import problema8.network.dto.AgeGroupDTO;
import problema8.network.dto.UserDTO;

public class Request {
    private RequestType type;
    private UserDTO user;
    private AgeGroupDTO ageGroup;
    private Long age_group_id;
    private String nume;
    private String cnp;
    private Event eventName1;
    private Long id;

    public Request() {}
    public RequestType getType() {
        return type;
    }
    public void setType(RequestType type) {
        this.type = type;
    }
    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }
    public AgeGroupDTO getAgeGroup() {
        return ageGroup;
    }
    public void setAgeGroup(AgeGroupDTO ageGroup) {
        this.ageGroup = ageGroup;
    }
    public Long getAge_group_id() {
        return age_group_id;
    }
    public void setAge_group_id(Long age_group_id) {
        this.age_group_id = age_group_id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public void setEventName1(Event eventName1) {
        this.eventName1 = eventName1;
    }

    public String getNume() {
        return nume;
    }

    public String getCnp() {
        return cnp;
    }

    public Event getEventName1() {
        return eventName1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
