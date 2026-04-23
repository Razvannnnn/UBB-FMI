namespace Networking.dto;

[Serializable]
public class UserDTO
{
    public string Username { get; set; }
    public string Password { get; set; }

    public UserDTO(string username, string password)
    {
        Username = username;
        Password = password;
    }

    public override string ToString()
    {
        return $"UserDTO[{Username} {Password}]";
    }
}
