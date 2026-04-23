package chat.network.utils;

import chat.network.rpcprotocol.ChatClientRpcReflectionWorker;
import chat.services.IChatServices;

import java.net.Socket;


public class ChatRpcConcurrentServer extends AbsConcurrentServer {
    private IChatServices chatServer;
    public ChatRpcConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        ChatClientRpcReflectionWorker worker=new ChatClientRpcReflectionWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }
}
