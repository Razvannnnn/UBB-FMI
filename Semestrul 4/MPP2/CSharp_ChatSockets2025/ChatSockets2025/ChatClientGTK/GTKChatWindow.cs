using Gtk;
using log4net;

namespace ChatClientGTK;

 public class ChatWindow:Window
    {
        private readonly GTKClientCtrl ctrl;
       
        private readonly ListStore friendsStore;
        private readonly ListStore messageStore;
        private Button trimite, logout;
        private Entry textMesaj;
        private TreeView messagesList, friendsList;
        private static readonly ILog log = LogManager.GetLogger(typeof(ChatWindow));
        
        public ChatWindow(GTKClientCtrl ctrl, String title) : base(title)
        {
           this.ctrl = ctrl;
           
            friendsStore=new ListStore(typeof(string));
          
            messageStore=new ListStore(typeof(string));

            IList<String> onlineFriends = ctrl.getLoggedFriends();
            foreach (var friend in onlineFriends)
            {
                friendsStore.AppendValues(friend);
            }
            //friendsStore.AppendValues("friend1 ");
            //friendsStore.AppendValues("friend2 ");
            SetDefaultSize(500,500);
            SetPosition(WindowPosition.Center);
            
           // VBox vbox = new VBox(false, 1);
            Box vbox = new Box(Orientation.Vertical, 1);
            Table table = new Table(10, 7, false);
            //lista pentru mesaje
            ScrolledWindow sw=new ScrolledWindow();
            sw.ShadowType = ShadowType.EtchedIn;
            sw.SetPolicy(PolicyType.Automatic, PolicyType.Automatic);
            sw.Add(messagesList=new TreeView(messageStore));
            table.Attach(sw, 0, 4, 0, 7);
            CellRendererText rendererText = new CellRendererText();
            TreeViewColumn column = new TreeViewColumn("Messages", rendererText, "text",0);
            messagesList.AppendColumn(column);
            messagesList.HeadersVisible = true;
            
            //lista pentru prieteni
            ScrolledWindow sw2=new ScrolledWindow();
            sw2.ShadowType = ShadowType.EtchedIn;
            sw2.SetPolicy(PolicyType.Automatic, PolicyType.Automatic);
            sw2.Add(friendsList=new TreeView(friendsStore));
            table.Attach(sw2, 5, 7, 1, 7);
            
            rendererText = new CellRendererText();
             column = new TreeViewColumn("Online Friends", rendererText, "text",0);
             friendsList.AppendColumn(column);
            //pentru mesaj
            table.Attach(new Label("Mesaj"), 0, 2, 7, 8);
            table.Attach(textMesaj=new Entry(), 3, 7, 7, 8);

            //pentru butoane
            table.Attach(logout=new Button("Logout"), 0, 3, 8, 9);
            table.Attach(trimite=new Button("Trimite mesaj"), 5, 7, 8, 9);

            trimite.Clicked += trimiteButtonPressed;
            logout.Clicked += logoutButtonPressed;
             DeleteEvent += closeWindow;

            vbox.PackEnd(table, true, true, 0);
        
                       
            Add(vbox);
            
            ctrl.updateEvent += userUpdate;
            
        }

        void trimiteButtonPressed(object? sender, EventArgs e)
        {
            string mesaj = textMesaj.Text;
            log.DebugFormat("Se trimite mesajul {0}",mesaj);
            if (mesaj.Trim() == "")
            {
               
                MessageDialog md = new MessageDialog(this, DialogFlags.DestroyWithParent, MessageType.Error, ButtonsType.Ok, "You must introduce some text!");
                md.Run();
                md.Dispose();
               
                return;

            }

            TreeIter selected;
            bool result=friendsList.Selection.GetSelected(out selected);
            log.DebugFormat("Selected result {0}", result);
            if (!result)
            { 
                MessageDialog md = new MessageDialog(this, DialogFlags.DestroyWithParent, MessageType.Error, ButtonsType.Ok, "You must select a friend!");
                md.Run();
                md.Dispose();
                return;  
            }

            String friendId = (string)friendsList.Model.GetValue(selected, 0);
           log.DebugFormat("Valoarea selectata {0}",friendId );
            ctrl.sendMessage(friendId, mesaj);
            textMesaj.Text = "";

        }
        
        void logoutButtonPressed(object? sender, EventArgs e)
        {
            log.DebugFormat("S-a apasat logout, se inchide aplicatia");
            ctrl.logout();
            ctrl.updateEvent -= userUpdate;
            Application.Quit();
        }
        
        void closeWindow(Object o, DeleteEventArgs args)
        {
            ctrl.logout();
            log.DebugFormat("Application closing");
            ctrl.updateEvent -= userUpdate;
            Application.Quit();
        }
        
        public void userUpdate(object? sender, ChatUserEventArgs e)
        {
            if (e.UserEventType==ChatUserEvent.FriendLoggedIn)
            {
                String friendId = e.Data.ToString();
                //adauga modificarea pe threadul care a creat aplicatia
                Application.Invoke (delegate {
                    friendsStore.AppendValues(friendId);
                });
               
               log.DebugFormat("[ChatWindow] friendLoggedIn {0}", friendId);
            

            }
            if (e.UserEventType == ChatUserEvent.FriendLoggedOut)
            {
                
                String friendId = e.Data.ToString();
                log.DebugFormat("[ChatWindow] friendLoggedOut {0}", friendId);
                TreeIter it;
                bool result=friendsStore.GetIterFirst(out it);
                for (int i = 0; i < friendsStore.IterNChildren(); i++)
                {
                    String value = (string)friendsStore.GetValue(it, 0);
                    log.DebugFormat("In for, am obtinut {0}",value);
                    if (value == friendId)
                    {
                        log.DebugFormat("Found friend {0}",value);
                        break;

                    }
                
                    friendsStore.IterNext(ref it);
                }
                //adauga modificarea pe threadul care a creat aplicatia
                Application.Invoke (delegate {
                    friendsStore.Remove(ref it);
                });
                    
            }
            if (e.UserEventType == ChatUserEvent.NewMessage)
            {
                String messString = e.Data.ToString();
                //adauga modificarea pe threadul care a creat aplicatia
                Application.Invoke (delegate {
                    messageStore.AppendValues(messString);
                });
                log.DebugFormat("[ChatWindow] messString " + messString);
               
            }
        }

    }