namespace chat.networking.dto;

[Serializable]
public class UserDTO
{
    public string Id { get; set; }
    public string Passwd { get; set; }
    
    public UserDTO(){}
    
    public UserDTO(string id) : this(id,"")
    {
    }
    public UserDTO(string id, string passwd)
    {
        Id = id;
        Passwd = passwd;
    }
}