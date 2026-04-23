using System.Net.Sockets;
using System.Reflection;
using log4net;
using log4net.Config;
using Networking.jsonprotocol;
using Networking.utils;
using Persistence;
using Problema8SC_CSharp.Persistence;
using Problema8SC_CSharp.Services;

using System.Configuration;

namespace Server;

internal class StartServer
{
    private static readonly int DEFAULT_PORT = 55556;
    private static readonly string DEFAULT_IP = "127.0.0.1";
    private static readonly ILog log = LogManager.GetLogger(typeof(StartServer));

    private static void Main(string[] args)
    {
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));

        Console.WriteLine("Starting server...");
        log.Info("Starting chat server");
        log.Info("Reading properties from app.config ...");
        var port = DEFAULT_PORT;
        var ip = DEFAULT_IP;
        string portS = ConfigurationManager.AppSettings["port"];
        if (portS == null)
        {
            log.Debug("Port property not set. Using default value " + DEFAULT_PORT);
        }
        else
        {
            var result = int.TryParse(portS, out port);
            if (!result)
            {
                log.Debug("Port property not a number. Using default value " + DEFAULT_PORT);
                port = DEFAULT_PORT;
                log.Debug("Portul " + port);
            }
        }

        string ipS = ConfigurationManager.AppSettings["ip"];

        if (ipS == null) log.Info("Port property not set. Using default value " + DEFAULT_IP);
        log.InfoFormat("Configuration Settings for database {0}", GetConnectionStringByName("Concurs"));
        IDictionary<string, string> props = new SortedList<string, string>();
        props.Add("ConnectionString", GetConnectionStringByName("Concurs"));

        IRepoUser userRepo = new RepoUser(props);
        IRepoChild childRepo = new RepoChild(props);
        IRepoAgeGroup ageGroupRepo = new RepoAgeGroup(props);
        IRepoEnrollment enrollmentRepo = new RepoEnrollment(props);
        IRepoEvent eventRepo = new RepoEvent(props);
        IServices serviceImpl = new ServiceImpl(ageGroupRepo, userRepo, childRepo, eventRepo, enrollmentRepo);

        Console.WriteLine("Starting server on IP {0} and port {1}", ip, port);
        log.DebugFormat("Starting server on IP {0} and port {1}", ip, port);

        //SerialChatServer server = new SerialChatServer(ip,port, serviceImpl);
        var server = new JsonChatServer(ip, port, serviceImpl);
        server.Start();
        Console.WriteLine("Server started");
        log.Debug("Server started ...");
        //Console.WriteLine("Press <enter> to exit...");
        Console.ReadLine();
    }

    private static string GetConnectionStringByName(string name)
    {
        // Assume failure.
        string returnValue = null;

        // Look for the name in the connectionStrings section.
        ConnectionStringSettings settings = ConfigurationManager.ConnectionStrings[name];

        // If found, return the connection string.
        if (settings != null)
            returnValue = settings.ConnectionString;

        return returnValue;
    }
}

public class JsonChatServer : ConcurrentServer
{
    private static readonly ILog log = LogManager.GetLogger(typeof(JsonChatServer));
    private readonly IServices server;
    private ClientJsonWorker worker;

    public JsonChatServer(string host, int port, IServices server) : base(host, port)
    {
        this.server = server;
        log.Debug("Creating JsonServer...");
    }

    protected override Thread createWorker(TcpClient client)
    {
        worker = new ClientJsonWorker(server, client);
        return new Thread(worker.Run);
    }
}