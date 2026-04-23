package chat.persistence;

import chat.model.User;


public interface UserRepository extends ICrudRepository<String,User> {
    User findBy(String username);
    User findBy(String username, String pass);
    Iterable<User> getFriendsOf(User user);
}
