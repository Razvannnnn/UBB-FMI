package chat.client;

import chat.client.gui.ChatClientCtrl;
import chat.client.gui.LoginWindow;
import chat.network.objectprotocol.ChatServerObjectProxy;
import chat.network.rpcprotocol.ChatServerRpcProxy;
import chat.services.IChatServer;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by grigo on 2/25/16.
 */
public class StartRpcClient {
    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";
    public static void main(String[] args) {
        Properties clientProps=new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/chatclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties "+e);
            return;
        }
        String serverIP=clientProps.getProperty("chat.server.host",defaultServer);
        int serverPort=defaultChatPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("chat.server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultChatPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);
        IChatServer server=new ChatServerRpcProxy(serverIP, serverPort);

        ChatClientCtrl ctrl=new ChatClientCtrl(server);


        LoginWindow logWin=new LoginWindow("Chat XYZ", ctrl);
        logWin.setSize(200,200);
        logWin.setLocation(150,150);
        logWin.setVisible(true);

    }
}
