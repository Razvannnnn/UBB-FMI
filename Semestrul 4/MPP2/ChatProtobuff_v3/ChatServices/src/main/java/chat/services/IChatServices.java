package chat.services;

import chat.model.Message;
import chat.model.User;


public interface IChatServices {
     void login(User user, IChatObserver client) throws ChatException;
     void sendMessage(Message message) throws ChatException;
     void logout(User user, IChatObserver client) throws ChatException;
     User[] getLoggedFriends(User user) throws ChatException;
}
