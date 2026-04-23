using chat.networking.jsonprotocol;
using Gtk;
using log4net;

namespace ChatClientGTK;

public class LoginWindow : Window
    {
        private GTKClientCtrl ctrl;
        Entry username, password;
        Button login, cancel;
        private static readonly ILog log = LogManager.GetLogger(typeof(LoginWindow));
        public LoginWindow(GTKClientCtrl ctrl) : base("Chat XYZ Login")
        {
            this.ctrl = ctrl;
            SetDefaultSize(300,300);
            SetPosition(WindowPosition.Center);
            DeleteEvent += CloseWindow;
            
           // VBox vbox = new VBox(false, 2);
            Box vbox=new Box(Orientation.Vertical,2);
            Table table = new Table(4, 4, false);
            //Grid grid = new Grid();
           table.Attach(new Label("Chat XYZ"), 0, 4, 0, 1);
            //grid.Attach(new Label("Chat XYZ"), 0, 0, 4, 2);
           table.Attach(new Label("Username"), 0, 2, 1, 2);
            //grid.Attach(new Label("Username"), 1, 2, 2, 1);
            table.Attach(username=new Entry(), 2, 4, 1, 2);
            //grid.Attach(username=new Entry(), 4, 2, 2, 1);  
           
            table.Attach(new Label("Password"), 0, 2, 2, 3);
            //grid.Attach(new Label("Password"), 0, 2, 2, 3);
            table.Attach(password=new Entry(), 2, 4, 2, 3);
            //grid.Attach(password=new Entry(), 2, 4, 2, 3);
            password.Visibility=false;
                       
            table.Attach(cancel=new Button("Clear"), 0, 2, 3, 4);
            //grid.Attach(cancel=new Button("Clear"), 0, 2, 3, 4);
            table.Attach(login=new Button("Login"), 2, 4, 3, 4);
            //grid.Attach(login=new Button("Login"), 2, 4, 3, 4); 

            login.Clicked += loginButtonPressed;
            cancel.Clicked += cancelButtonPressed;

            vbox.PackEnd(table, true, true, 0);
            //vbox.PackEnd(grid, true, true, 0);
            
            Add(vbox);
           
        }

        void CloseWindow(Object o, DeleteEventArgs args)
        {
           
            log.Debug("Application closing");
            Application.Quit();
        }
        
         void loginButtonPressed(object? sender, EventArgs e)
        {
            log.Debug("S-a apasat login");
            log.DebugFormat("Username {0} - Password - {1}",username.Text, password.Text);
            String user = username.Text;
            String pass = password.Text;
            try
            {
                ctrl.login(user, pass);
                log.Debug("Login succeded");
                ChatWindow chatWin=new ChatWindow(ctrl, "Chat window for " + user);
                chatWin.ShowAll();
                Dispose();
            }catch(Exception ex)
            {
                log.Error(ex);
                MessageDialog md = new MessageDialog(this, DialogFlags.DestroyWithParent, MessageType.Error, ButtonsType.Close, "Eroare la autentificare"+ex.Message);
                md.Run();
                md.Dispose();
            }
        }

         void cancelButtonPressed(object? sender, EventArgs e)
         {
             username.Text = "";
             password.Text = "";
             log.Debug("Cancel button clicked");
             // Application.Quit();
         }
    }