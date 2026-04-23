using chat.model;

namespace chat.persistence;

public class UserRepositoryMock: IUserRepository{
            private readonly IDictionary<String, User> allUsers;

            public UserRepositoryMock() {
                allUsers=new Dictionary<String, User>();
                populateUsers();
            }
            public User findBy(string username, string pass)
            {
                User userR;
                bool  ok=allUsers.TryGetValue(username,out userR);
                if (!ok || userR.Password!=pass)
                    return null;
                return new User(username, "",userR.Name);
            }

            public IEnumerable<User> getFriendsOf(User user)
            {
                User userR;
                if (allUsers.TryGetValue(user.Id, out userR))
                    return userR.Friends;
                return new User[0];
            }
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

                allUsers[ana.Id]=ana;
                allUsers[mihai.Id]= mihai;
                allUsers[ion.Id]= ion;
                allUsers[maria.Id]= maria;
                allUsers[test.Id]= test;


            }


            public void save(User entity)
            {

            }

            public void delete(string id)
            {
                throw new NotImplementedException();
            }

            public User findOne(string id)
            {
                throw new NotImplementedException();
            }

            public void update(string id, User e)
            {
                throw new NotImplementedException();
            }

            public IEnumerable<User> getAll()
            {
                throw new NotImplementedException();
            }

            public User findBy(string username)
            {
                throw new NotImplementedException();
            }


        }