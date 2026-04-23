using chat.model;
using chat.services;
using log4net;

namespace ChatClientGTK;

 public class GTKClientCtrl: IChatObserver
    {
        public event EventHandler<ChatUserEventArgs>? updateEvent; //ctrl calls it when it has received an update
        private readonly IChatServices server;
        private User? currentUser;
        private static readonly ILog log = LogManager.GetLogger(typeof(GTKClientCtrl));
        public GTKClientCtrl(IChatServices server)
        {
            this.server = server;
            currentUser = null;
        }

        public void login(String userId, String pass)
        {
            User user=new User(userId,pass);
            server.login(user,this);
            log.Debug("Login succeeded ....");
            currentUser = user;
            log.DebugFormat("Current user {0}", user);
        }
        public void messageReceived(Message message)
        {
            String mess = "[" + message.Sender.Id + "]: " + message.Text;
            ChatUserEventArgs userArgs=new ChatUserEventArgs(ChatUserEvent.NewMessage,mess);
            log.Debug("Message received");
            OnUserEvent(userArgs);
        }

        public void friendLoggedIn(User friend)
        {
            log.Debug("Friend logged in "+friend);
            ChatUserEventArgs userArgs = new ChatUserEventArgs(ChatUserEvent.FriendLoggedIn, friend.Id);
            OnUserEvent(userArgs);
        }

        public void friendLoggedOut(User friend)
        {
            log.Debug("Friend logged out"+friend);
            ChatUserEventArgs userArgs = new ChatUserEventArgs(ChatUserEvent.FriendLoggedOut, friend.Id);
            OnUserEvent(userArgs);
        }

        public void logout()
        {
            log.Debug("Ctrl logout");
            server.logout(currentUser, this);
            currentUser = null;
        }

        protected virtual void OnUserEvent(ChatUserEventArgs e)
        {
           
            updateEvent?.Invoke(this, e);
            log.Debug("Update Event called");
        }
        public IList<String> getLoggedFriends()
        {
            IList<String> loggedFriends=new List<string>();
            User[] friends = server.getLoggedFriends(currentUser);
            foreach (var user in friends)
            {
                loggedFriends.Add(user.Id);
            }
            return loggedFriends;
        }

        public void sendMessage(string id, string txt)
        {
            //display the sent message on the user window
            String mess = "[" + currentUser?.Id + "-->"+id+"]: " + txt;
            ChatUserEventArgs userArgs = new ChatUserEventArgs(ChatUserEvent.NewMessage, mess);
            OnUserEvent(userArgs);
            //sends the message to the server
            User receiver=new User(id);
            Message message=new Message(currentUser,receiver, txt);
            server.sendMessage(message);
        }
        
    }