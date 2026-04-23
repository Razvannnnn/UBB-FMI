using System.Configuration;
using System.Reflection;
using chat.networking.jsonprotocol;
using chat.networking.protocol;
using chat.services;
using Gtk;
using log4net;
using log4net.Config;

namespace ChatClientGTK;

internal class StartGTKClient
    
{
    private static int DEFAULT_PORT=55556;
    private static String DEFAULT_IP="127.0.0.1";
    private static readonly ILog log = LogManager.GetLogger(typeof(StartGTKClient));
    public static void Main(string[] args)
    {
            
       // var systemPath = Environment.GetEnvironmentVariable ("PATH", EnvironmentVariableTarget.Machine);
        //var gtkPath = "/usr/local/Cellar/gtk+3/3.24.42"; // look where gtk is located, and set this path
        //Environment.SetEnvironmentVariable ("PATH", $"{gtkPath};{systemPath}");
        //Console.Write("System Path: {0}",systemPath);
        
        
        //configurare jurnalizare folosind log4net
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));
        
        
        Application.Init ();
        log.Debug("Reading properties from app.config ...");
        int port = DEFAULT_PORT;
        String ip = DEFAULT_IP;
        String portS= ConfigurationManager.AppSettings["port"];
        if (portS == null)
        {
            log.DebugFormat("Port property not set. Using default value {0}",DEFAULT_PORT);
        }
        else
        {
            bool result = Int32.TryParse(portS, out port);
            if (!result)
            {
                log.DebugFormat("Port property not a number. Using default value {0}",DEFAULT_PORT);
                port = DEFAULT_PORT;
               log.DebugFormat("Portul {0}",port);
            }
        }
        String ipS=ConfigurationManager.AppSettings["ip"];
           
        if (ipS == null)
        {
            log.DebugFormat("Port property not set. Using default value {0}",DEFAULT_IP);
        }
            
        log.InfoFormat("Using  server on IP {0} and port {1}", ip, port);
       // IChatServices server = new ChatServerProxy(ip, port);
        IChatServices server = new ChatServerJsonProxy(ip, port);
        GTKClientCtrl ctrl=new GTKClientCtrl(server);
        Window w=new LoginWindow(ctrl);
        // Window w = new ChatWindow();
        w.ShowAll();
        Application.Run ();
                        
    }
}