using chat.model;
using log4net;

namespace chat.persistence;

  public class UserRepositoryDb:IUserRepository
        {
            IDictionary<String, string> props;
            private static readonly ILog log = LogManager.GetLogger(typeof(UserRepositoryDb));

            public UserRepositoryDb(IDictionary<String, string> props)
            {
                this.props = props;
            }
            public void save(User entity)
            {

            }

            public void delete(string id)
            {

            }

            public User findOne(string id)
            {
                return null;

            }

            public void update(string id, User e)
            {

            }

            public IEnumerable<User> getAll()
            {
                return null;
            }

            public User findBy(string username)
            {
                return null;

            }

            public User findBy(string username, string pass)
            {
                log.DebugFormat("UserDB [findBy] username={0}  passowrd={1} ",username,pass);
                var con=DBUtils.getConnection(props);

                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select name from users where username=@id and password=@pass";
                    var paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = username;
                    comm.Parameters.Add(paramId);
                    var paramPass = comm.CreateParameter();
                    paramPass.ParameterName = "@pass";
                    paramPass.Value = PasswordUtils.HashText(pass);
                    comm.Parameters.Add(paramPass);
                    using (var dataR = comm.ExecuteReader())
                    {
                        if (dataR.Read())
                        {
                            User user=new User(username);
                            user.Name = dataR.GetString(0);
                            log.DebugFormat("Found user with name {0}", user.Name);
                            return user;
                        }
                    }
                }

                return null;
            }

            public IEnumerable<User> getFriendsOf(User user)
            {
                log.DebugFormat("UserDB [getFriendsOf] user={0}",user);
                var con=DBUtils.getConnection(props);
                List<User> friends=new List<User>();
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select friends.userA, friends.userB  from friends where  friends.userA=@userA or friends.userB=@userB";
                     var paramUserA = comm.CreateParameter();
                    paramUserA.ParameterName = "@userA";
                    paramUserA.Value = user.Id;
                    comm.Parameters.Add(paramUserA);
                    var paramUserB= comm.CreateParameter();
                    paramUserB.ParameterName = "@userB";
                    paramUserB.Value = user.Id;
                    comm.Parameters.Add(paramUserB);
                    using (var dataR = comm.ExecuteReader())
                    {
                        while (dataR.Read())
                        {
                            User userR=new User(dataR.GetString(0)==user.Id?dataR.GetString(1):dataR.GetString(0));
                           // user.Name = dataR.GetString("name");
                            friends.Add(userR);
                        }
                    }
                }
                log.DebugFormat("Exiting UserDB [getFriendsOf] with friend ={0}",friends);
                return friends;
            }
        }
