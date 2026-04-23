using System.Configuration;
using System.Net.Sockets;
using System.Reflection;
using chat.networking;
using chat.networking.jsonprotocol;
using chat.networking.protocol;
using chat.persistence;
using chat.server;
using chat.services;
using log4net;
using log4net.Config;

class StartServer
    {
        private static int DEFAULT_PORT=55556;
        private static String DEFAULT_IP="127.0.0.1";
        private static readonly ILog log = LogManager.GetLogger(typeof(StartServer));
        static void Main(string[] args)
        {
            
          
            //configurare jurnalizare folosind log4net
            var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
            XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));
			
            log.Info("Starting chat server");
            log.Info("Reading properties from app.config ...");
           int port = DEFAULT_PORT;
           String ip = DEFAULT_IP;
           String portS= ConfigurationManager.AppSettings["port"];
           if (portS == null)
           {
               log.Debug("Port property not set. Using default value "+DEFAULT_PORT);
           }
           else
           {
               bool result = Int32.TryParse(portS, out port);
               if (!result)
               {
                   log.Debug("Port property not a number. Using default value "+DEFAULT_PORT);
                   port = DEFAULT_PORT;
                   log.Debug("Portul "+port);
               }
           }
           String ipS=ConfigurationManager.AppSettings["ip"];
           
           if (ipS == null)
           {
               log.Info("Port property not set. Using default value "+DEFAULT_IP);
           }
           log.InfoFormat("Configuration Settings for database {0}",GetConnectionStringByName("chatDB"));
           IDictionary<String, string> props = new SortedList<String, String>();
           props.Add("ConnectionString", GetConnectionStringByName("chatDB"));
            IUserRepository userRepo=new UserRepositoryDb(props);
            IMessageRepository messageRepository=new MessageRepositoryDb(props);
            //IUserRepository userRepo=new UserRepositoryMock();
            //IMessageRepository messageRepository=new MessageRepositoryMock();
            IChatServices serviceImpl = new ChatServerImpl(userRepo, messageRepository);

         
           log.DebugFormat("Starting server on IP {0} and port {1}", ip, port);
			//SerialChatServer server = new SerialChatServer(ip,port, serviceImpl);
            JsonChatServer server = new JsonChatServer(ip,port, serviceImpl);
            server.Start();
            log.Debug("Server started ...");
            //Console.WriteLine("Press <enter> to exit...");
            Console.ReadLine();
            
        }
        
        
		
        static string GetConnectionStringByName(string name)
        {
            // Assume failure.
            string returnValue = null;

            // Look for the name in the connectionStrings section.
            ConnectionStringSettings settings =ConfigurationManager.ConnectionStrings[name];

            // If found, return the connection string.
            if (settings != null)
                returnValue = settings.ConnectionString;

            return returnValue;
        }
    }

   public class SerialChatServer: ConcurrentServer 
    {
        private IChatServices server;
        private ChatClientWorker worker;
        public SerialChatServer(string host, int port, IChatServices server) : base(host, port)
            {
                this.server = server;
                Console.WriteLine("SerialChatServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new ChatClientWorker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }
    
   public class JsonChatServer: ConcurrentServer 
   {
       private IChatServices server;
       private ChatClientJsonWorker worker;
       private static readonly ILog log = LogManager.GetLogger(typeof(JsonChatServer));
       public JsonChatServer(string host, int port, IChatServices server) : base(host, port)
       {
           this.server = server;
           log.Debug("Creating JsonChatServer...");
       }
       protected override Thread createWorker(TcpClient client)
       {
           worker = new ChatClientJsonWorker(server, client);
           return new Thread(worker.run);
       }
   }
    