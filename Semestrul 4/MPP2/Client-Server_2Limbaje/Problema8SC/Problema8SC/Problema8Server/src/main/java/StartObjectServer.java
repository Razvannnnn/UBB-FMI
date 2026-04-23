import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.network.utils.AbstractServer;
import problema8.network.utils.JsonConcurrentServer;
import problema8.persistence.jdbc.*;
import problema8.server.ServiceImpl;
import problema8.services.IService;

import java.util.Properties;

public class StartObjectServer {
    private static int defaultPort=55555;
    private static Logger logger = LogManager.getLogger(StartObjectServer.class);

    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/problema8server.properties"));
            logger.info("Server properties set. {} ", serverProps);
        } catch (Exception e) {
            logger.error("Cannot find problema8server.properties "+e);
            logger.debug("Looking for file in "+(new java.io.File(".")).getAbsolutePath());
            return;
        }
        RepoUser userRepo=new RepoUser(serverProps);
        RepoChild childRepo=new RepoChild(serverProps);
        RepoAgeGroup ageGroupRepo=new RepoAgeGroup(serverProps);
        RepoEnrollment enrollmentRepo=new RepoEnrollment(serverProps);
        RepoEvent eventRepo=new RepoEvent(serverProps);
        IService service=new ServiceImpl(ageGroupRepo, userRepo, childRepo, eventRepo, enrollmentRepo);
        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("problema8.server.port"));
        } catch (NumberFormatException nef) {
            logger.error("Wrong Port Number"+nef.getMessage());
            logger.debug("Using default port "+defaultPort);
        }
        logger.debug("Starting server on port: "+serverPort);
        AbstractServer server = new JsonConcurrentServer(serverPort, service);
        try {
            server.start();
        } catch (Exception e) {
            logger.error("Error starting the server" + e.getMessage());
        }
    }
}
