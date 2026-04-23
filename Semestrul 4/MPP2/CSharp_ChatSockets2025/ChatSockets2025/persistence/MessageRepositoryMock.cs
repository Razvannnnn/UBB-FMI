using chat.model;

namespace chat.persistence;

public class MessageRepositoryMock:IMessageRepository
{
    public void save(Message entity)
    {
    }

    public void delete(int id)
    {
    }

    public Message findOne(int id)
    {
        return null;
    }

    public void update(int id, Message e)
    {

    }

    public IEnumerable<Message> getAll()
    {
        return null;
    }
}