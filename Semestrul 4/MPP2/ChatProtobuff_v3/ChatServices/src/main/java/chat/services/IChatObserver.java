package chat.services;

import chat.model.Message;
import chat.model.User;


public interface IChatObserver {
     void messageReceived(Message message) throws ChatException;
     void friendLoggedIn(User friend) throws ChatException;
     void friendLoggedOut(User friend) throws ChatException;
}
