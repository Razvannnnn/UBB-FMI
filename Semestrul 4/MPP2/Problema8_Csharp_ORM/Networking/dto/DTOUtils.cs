namespace Networking.dto;

using Problema8SC_CSharp.Model;

public static class DTOUtils
{
    public static User GetFromDTO(UserDTO userDTO)
    {
        string username = userDTO.Username;
        string password = userDTO.Password;
        return new User(username, password);
    }

    public static UserDTO GetDTO(User user)
    {
        string username = user.Username;
        string password = user.Password;
        return new UserDTO(username, password);
    }

    public static UserDTO[] GetDTO(User[] users)
    {
        UserDTO[] userDTOs = new UserDTO[users.Length];
        for (int i = 0; i < users.Length; i++)
        {
            userDTOs[i] = GetDTO(users[i]);
        }
        return userDTOs;
    }

    public static User[] GetFromDTO(UserDTO[] userDTOs)
    {
        User[] users = new User[userDTOs.Length];
        for (int i = 0; i < userDTOs.Length; i++)
        {
            users[i] = GetFromDTO(userDTOs[i]);
        }
        return users;
    }

    public static AgeGroup GetFromDTO(AgeGroupDTO ageGroupDTO)
    {
        return new AgeGroup(ageGroupDTO.Name, ageGroupDTO.MinAge, ageGroupDTO.MaxAge);
    }

    public static AgeGroupDTO GetDTO(AgeGroup ageGroup)
    {
        return new AgeGroupDTO(ageGroup.Name, ageGroup.MinAge, ageGroup.MaxAge);
    }

    public static ChildDTO GetDTO(Child child)
    {
        return new ChildDTO(child.Name, child.CNP);
    }

    public static EnrollmentDTO GetDTO(Enrollment enrollment)
    {
        return new EnrollmentDTO(enrollment.Child.GetId(), enrollment.Event.GetId());
    }

    public static EventDTO GetDTO(Event eventObj)
    {
        return new EventDTO(eventObj.Name, eventObj.Distance, eventObj.AgeGroupId);
    }
}
