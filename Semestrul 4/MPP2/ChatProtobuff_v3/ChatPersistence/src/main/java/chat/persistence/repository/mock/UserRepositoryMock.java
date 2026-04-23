package chat.persistence.repository.mock;

import chat.model.User;
import chat.persistence.UserRepository;

import java.util.*;


public class UserRepositoryMock implements UserRepository{
    private Map<String, User> allUsers;

    public UserRepositoryMock() {
        allUsers=new TreeMap<String, User>();
        populateUsers();
    }


    private static final Set<User> noFriends=new TreeSet<>();


    private void populateUsers(){
        User ana=new User("ana", "ana", "Popescu Ana");
        User mihai=new User("mihai", "mihai", "Ionescu Mihai");
        User ion=new User("ion", "ion", "Vasilescu Ion");
        User maria=new User("maria", "maria", "Marinescu Maria");
        User test=new User("test", "test", "Test user");

        ana.addFriend(mihai);
        ana.addFriend(test);

        mihai.addFriend(ana);
        mihai.addFriend(test);
        mihai.addFriend(ion);

        ion.addFriend(maria);
        ion.addFriend(test);
        ion.addFriend(mihai);

        maria.addFriend(ion);
        maria.addFriend(test);

        test.addFriend(ana);
        test.addFriend(mihai);
        test.addFriend(ion);
        test.addFriend(maria);

        allUsers.put(ana.getId(),ana);
        allUsers.put(mihai.getId(), mihai);
        allUsers.put(ion.getId(), ion);
        allUsers.put(maria.getId(), maria);
        allUsers.put(test.getId(), test);


    }

    @Override
    public User findBy(String username) {
        User userR=allUsers.get(username);
        if (userR==null)
            return null;
        User res=new User(username);
        res.setName(userR.getName());
        return res;

    }

    @Override
    public User findBy(String username, String pass) {
        User userR=allUsers.get(username);
        if ((userR!=null)&&(userR.getPasswd().equals(pass)) ){
            User res = new User(username);
            res.setName(userR.getName());
            return res;
        }
        return null;

    }

    @Override
    public Iterable<User> getFriendsOf(User user) {
        User userR=allUsers.get(user.getId());
        if (userR!=null)
            return userR.getFriends();

        return noFriends;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(String s) {
        if (allUsers.containsKey(s))
            allUsers.remove(s);
    }

    @Override
    public User findOne(String s) {
        return findBy(s);
    }

    @Override
    public void update(String s, User user) {

    }

    @Override
    public Iterable<User> getAll() {
        return allUsers.values();
    }
}
