package problema8.client;

import problema8.services.IService;

import java.io.IOException;
import java.util.Properties;

public class StartJsonClient {
    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";

    public static void main(String[] args) {
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/problema8client.properties"));
            System.out.println("Client properties set.");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }

        String server = clientProps.getProperty("server.host", defaultServer);
        int port = defaultChatPort;
        try {
            port = Integer.parseInt(clientProps.getProperty("server.port", String.valueOf(defaultChatPort)));
        } catch (NumberFormatException e) {
            System.err.println("Wrong port number " + e);
            return;
        }

        System.out.println("Using server " + server + " and port " + port);



    }
}
