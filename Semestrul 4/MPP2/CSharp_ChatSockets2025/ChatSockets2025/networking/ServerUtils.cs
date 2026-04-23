using System.Net;
using System.Net.Sockets;
using log4net;

namespace chat.networking;

public abstract class AbstractServer
{
    private TcpListener server;
    private String host;
    private int port;
    
    private static readonly ILog log = LogManager.GetLogger(typeof(AbstractServer));
    public AbstractServer(String host, int port)
    {
        this.host = host;
        this.port = port;
    }
    public void Start()
    {
        IPAddress adr = IPAddress.Parse(host);
        IPEndPoint ep=new IPEndPoint(adr,port);
        server=new TcpListener(ep);
        server.Start();
        while (true)
        {
            log.Debug("Waiting for clients ...");
            TcpClient client = server.AcceptTcpClient();
            log.Debug("Client connected ...");
            processRequest(client);
        }
    }

    public abstract void processRequest(TcpClient client);
        
}

    
public abstract class ConcurrentServer:AbstractServer
{
            
    public ConcurrentServer(string host, int port) : base(host, port)
    {}

    public override void processRequest(TcpClient client)
    {
                
        Thread t = createWorker(client);
        t.Start();
                
    }

    protected abstract  Thread createWorker(TcpClient client);
            
}

