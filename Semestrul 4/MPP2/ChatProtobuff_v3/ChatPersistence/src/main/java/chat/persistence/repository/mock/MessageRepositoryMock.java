package chat.persistence.repository.mock;

import chat.model.Message;
import chat.persistence.MessageRepository;

public class MessageRepositoryMock implements MessageRepository {
    @Override
    public void save(Message message) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Message findOne(Integer integer) {
        return null;
    }

    @Override
    public void update(Integer integer, Message message) {

    }

    @Override
    public Iterable<Message> getAll() {
        return null;
    }
}
