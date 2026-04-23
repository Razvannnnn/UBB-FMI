using chat.model;

namespace chat.persistence;

public interface IMessageRepository:ICrudRepository<int, Message>
{

}