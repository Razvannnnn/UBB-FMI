package problema8.network.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.network.jsonprotocol.ClientJsonWorker;
import problema8.services.IService;

import java.net.Socket;

public class JsonConcurrentServer extends AbsConcurrentServer {
    private IService server;
    private static Logger logger = LogManager.getLogger(JsonConcurrentServer.class);

    public JsonConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        logger.info("JsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientJsonWorker worker=new ClientJsonWorker(server, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
