using chat.networking.dto;

namespace chat.networking.jsonprotocol;

public class Response
{
    public ResponseType Type { get; set; }
    public string ErrorMessage { get; set; }
    public UserDTO User { get; set; }
    public UserDTO[] Friends { get; set; }
    public MessageDTO Message { get; set; }

    public Response()
    {
    }

    public override string ToString()
    {
        return string.Format("[type={0}, error={1}, text={2}]",Type,ErrorMessage,User);
    }
}