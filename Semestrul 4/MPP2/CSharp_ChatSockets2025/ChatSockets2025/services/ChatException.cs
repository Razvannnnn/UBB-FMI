namespace chat.services;

public class ChatException : Exception
{
    public ChatException():base() { }

    public ChatException(String msg) : base(msg) { }

    public ChatException(String msg, Exception ex) : base(msg, ex) { }

}