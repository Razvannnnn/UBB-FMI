namespace chat.networking.dto;

[Serializable]
public class MessageDTO
{
    public string SenderId { get; set; }
    public string ReceiverId { get; set; }
    public string Text { get; set; }

    public MessageDTO(string senderId, string text, string receiverId)
    {
        SenderId = senderId;
        Text = text;
        ReceiverId = receiverId;
    }
    
    public MessageDTO() { }

    public override string ToString()
    {
        return string.Format("[SenderId: {0}, ReceiverId: {1} Text={2}]", SenderId, ReceiverId, Text);
    }
}
