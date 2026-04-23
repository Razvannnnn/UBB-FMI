using Networking.dto;

namespace Networking.jsonprotocol;

[Serializable]
public class Response
{
    public ResponseType Type { get; set; }
    public string ErrorMessage { get; set; }
    public UserDTO User { get; set; }
    public ChildDTO Child { get; set; }
    public EnrollmentDTO Enrollment { get; set; }
    public EventDTO Event { get; set; }
    public object Data { get; set; }

    public Response() { }

    public ResponseType GetType()
    {
        return Type;
    }

    public void SetEvent(EventDTO eventObj)
    {
        Event = eventObj;
    }

    public EventDTO GetEvent()
    {
        return Event;
    }

    public void SetType(ResponseType type)
    {
        Type = type;
    }

    public string GetErrorMessage()
    {
        return ErrorMessage;
    }

    public void SetErrorMessage(string errorMessage)
    {
        ErrorMessage = errorMessage;
    }

    public UserDTO GetUser()
    {
        return User;
    }

    public void SetUser(UserDTO user)
    {
        User = user;
    }

    public ChildDTO GetChild()
    {
        return Child;
    }

    public void SetChild(ChildDTO child)
    {
        Child = child;
    }

    public object GetData()
    {
        return Data;
    }

    public void SetEnrollment(EnrollmentDTO enrollment)
    {
        Enrollment = enrollment;
    }

    public EnrollmentDTO GetEnrollment()
    {
        return Enrollment;
    }

    public void SetData(object newData)
    {
        Data = newData;
    }
}