package problema8.network.dto;

import problema8.model.*;

public class DTOUtils {
    public static User getFromDTO(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        return new User(username, password);
    }

    public static UserDTO getDTO(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        return new UserDTO(username, password);
    }

    public static UserDTO[] getDTO(User[] users) {
        UserDTO[] userDTOs = new UserDTO[users.length];
        for (int i = 0; i < users.length; i++) {
            userDTOs[i] = getDTO(users[i]);
        }
        return userDTOs;
    }

    public static User[] getFromDTO(UserDTO[] userDTOs) {
        User[] users = new User[userDTOs.length];
        for (int i = 0; i < userDTOs.length; i++) {
            users[i] = getFromDTO(userDTOs[i]);
        }
        return users;
    }

    public static AgeGroup getFromDTO(AgeGroupDTO ageGroupDTO) {
        return new AgeGroup(ageGroupDTO.getName(), ageGroupDTO.getMinAge(), ageGroupDTO.getMaxAge());
    }

    public static AgeGroupDTO getDTO(AgeGroup ageGroup) {
        return new AgeGroupDTO(ageGroup.getName(), ageGroup.getMinAge(), ageGroup.getMaxAge());
    }

    public static ChildDTO getDTO(Child child) {
        return new ChildDTO(child.getName(), child.getCNP());
    }

    public static EnrollmentDTO getDTO(Enrollment enrollment) {
        return new EnrollmentDTO(enrollment.getChild().getId(), enrollment.getEvent().getId());
    }

    public static EventDTO getDTO(Event event) {
        return new EventDTO(event.getName(), event.getDistance(), event.getAgeGroupId());
    }
}
