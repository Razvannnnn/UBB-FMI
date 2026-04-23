package chat.persistence.repository.mock;

import chat.model.User;
import chat.persistence.RepositoryException;
import chat.persistence.UserRepository;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class UserRepositoryMock implements UserRepository{
    private Map<String, User> allUsers;

    public UserRepositoryMock() {
        allUsers=new TreeMap<String, User>();
        populateUsers();
    }

    private static final Set<User> noFriends=new TreeSet<>();


    private void populateUsers(){
        User ana=new User("ana", "gdjIvfoS8c077r1x66mHhw==", "Popescu Ana");
        User mihai=new User("vasile", "sV77eaKyudjvixFfDigOXQ==", "Ionescu Vasile");
        User ion=new User("ion", "UMVVe/JxoeVLNKIHCkCDYQ==", "Vasilescu Ion");
        User maria=new User("maria", "f+sLy+0lHNXF9ulTCvXpMA==", "Marinescu Maria");
        User test=new User("test", "5l8G2J1C5uzgplqEhA5Kqw==", "Test user");

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
       // User res=new User(username);
       // res.setName(userR.getName());
        return userR;

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
    public User[] getUsers() {
        return allUsers.values().toArray(new User[allUsers.size()]);
    }

    @Override
    public void save(User user) {
        System.out.println("[UserRepositoryMock] save user - entering");
        if(allUsers.containsKey(user.getId()))
            throw new RepositoryException("Username already exists: "+user.getId());
        allUsers.put(user.getId(),user);
        System.out.println("[UserRepositoryMock] save user - exiting ok");
    }

    @Override
    public void delete(String s) {
        if (allUsers.containsKey(s))
            allUsers.remove(s);
        else
            throw new RepositoryException("Username ["+s+"] not found for deletion.");
    }

    @Override
    public User findOne(String s) {

        return findBy(s);
    }

    @Override
    public void update(String s, User user) {
        System.out.println("update user id "+s+" new value " +user );
        if (allUsers.containsKey(s)){
            if (s.equals(user.getId())) {
                allUsers.put(s, user);
                return;
            }

        }
         throw new RepositoryException("User could not be updated "+user);
    }

    @Override
    public Iterable<User> getAll() {
        return allUsers.values();
    }
}
