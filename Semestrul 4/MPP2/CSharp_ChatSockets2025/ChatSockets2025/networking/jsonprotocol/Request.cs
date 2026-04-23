using chat.networking.dto;

namespace chat.networking.jsonprotocol;

public class Request
{
    public RequestType Type { get; set; }
    public UserDTO User { get; set; }
    public MessageDTO Message { get; set; }
  

    public override string ToString()
    {
        return string.Format("Request[type={0}, user={1}, message={2}] ", Type, User, Message);
    }
}