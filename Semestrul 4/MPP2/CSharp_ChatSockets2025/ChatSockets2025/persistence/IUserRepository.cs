using chat.model;

namespace chat.persistence;

public interface IUserRepository:ICrudRepository<string,User>
{
    User findBy(String username);
    User findBy(String username, String pass);
    IEnumerable<User> getFriendsOf(User user);
}