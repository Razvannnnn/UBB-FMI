package problema8.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.client.gui.LoginController;
import problema8.client.gui.MainController;
import problema8.network.jsonprotocol.ServicesJsonProxy;
import problema8.services.IService;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartObjectClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    private static Logger logger = LogManager.getLogger(StartRpcClientFX.class);

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartJsonClientFX.class.getResourceAsStream("/problema8client.properties"));
            logger.info("Client properties set {} ",clientProps);
            clientProps.list(System.out);
        } catch (IOException e) {
            logger.error("Cannot find problema8client.properties " + e);
            logger.debug("Looking for chatclient.properties in folder {}",(new File(".")).getAbsolutePath());
            return;
        }
        String serverIP = clientProps.getProperty("problema8.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("problema8.server.port"));
        } catch (NumberFormatException ex) {
            logger.error("Wrong port number " + ex.getMessage());
            logger.debug("Using default port: " + defaultChatPort);
        }
        logger.info("Using server IP " + serverIP);
        logger.info("Using server port " + serverPort);

        IService server = new ServicesJsonProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();


        LoginController ctrl = loader.<LoginController>getController();
        ctrl.setService(server);

        FXMLLoader cloader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Parent croot=cloader.load();

        MainController mainCtrl = cloader.<MainController>getController();

        //

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root, 300, 130));
        primaryStage.show();
    }
}
