package mpp.hibernate.example.chat;

import java.util.List;

public interface ChatMessageRepository extends ICrudRepository<Integer, ChatMessage>{
    public List<ChatMessage> getMessagesFrom(String name);
}
