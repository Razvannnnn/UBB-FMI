using chat.model;
using chat.persistence;
using chat.services;
using log4net;

namespace chat.server;

 public class ChatServerImpl: IChatServices
    {
        private IUserRepository userRepository;
        private IMessageRepository messageRepository;
        private readonly IDictionary <String, IChatObserver> loggedClients;
        private static readonly ILog log = LogManager.GetLogger(typeof(ChatServerImpl));
    public ChatServerImpl(IUserRepository repo, IMessageRepository messageRepo) {
        userRepository = repo;
        messageRepository = messageRepo;
        loggedClients=new Dictionary<String, IChatObserver>();
    }

    public  void login(User user, IChatObserver client)  {
        User userOk=userRepository.findBy(user.Id,user.Password);
        if (userOk!=null){
            if(loggedClients.ContainsKey(user.Id))
                throw new ChatException("User already logged in.");
            loggedClients[user.Id]= client;
            notifyFriendsLoggedIn(user);
        }else
            throw new ChatException("Authentication failed.");
    }   

    private void notifyFriendsLoggedIn(User user){
        IEnumerable<User> friends=userRepository.getFriendsOf(user);
        log.Debug("notify logged friends "+friends.Count());
        foreach(User us in friends){
            if (loggedClients.ContainsKey(us.Id))
            {
                IChatObserver chatClient = loggedClients[us.Id];
				Task.Run(() => chatClient.friendLoggedIn(user));
            }
        }
    }

    private void notifyFriendsLoggedOut(User user) {
        IEnumerable<User> friends=userRepository.getFriendsOf(user);
        foreach(User us in friends){
            if (loggedClients.ContainsKey(us.Id))
            {
                IChatObserver chatClient = loggedClients[us.Id];
				Task.Run(() =>chatClient.friendLoggedOut(user));
            }
        }
    }

    public  void sendMessage(Message message)  {
        String id_receiver=message.Receiver.Id;
        log.Debug("sending message to "+id_receiver);
        if (loggedClients.ContainsKey(id_receiver))  {     //the receiver is logged in
            IChatObserver receiverClient=loggedClients[id_receiver];
			Task.Run(() => receiverClient.messageReceived(message));
            messageRepository.save(message);
        }
        else
            throw new ChatException("User "+id_receiver+" not logged in.");
    }

    public  void logout(User user, IChatObserver client) {
        IChatObserver localClient=loggedClients[user.Id];
        if (localClient==null)
            throw new ChatException("User "+user.Id+" is not logged in.");
        loggedClients.Remove(user.Id);
        notifyFriendsLoggedOut(user);
    }

    public  User[] getLoggedFriends(User user)  {
        IEnumerable<User> friends=userRepository.getFriendsOf(user);
        IList<User> result=new List<User>();
        log.Debug("Logged friends for: "+user.Id);
        foreach (User friend in friends){
            if (loggedClients.ContainsKey(friend.Id)){    //the friend is logged in
                result.Add(new User(friend.Id));
                log.Debug("+"+friend.Id);
            }
        }
        log.Debug("Size "+result.Count);
        return result.ToArray();
    }

       
    }