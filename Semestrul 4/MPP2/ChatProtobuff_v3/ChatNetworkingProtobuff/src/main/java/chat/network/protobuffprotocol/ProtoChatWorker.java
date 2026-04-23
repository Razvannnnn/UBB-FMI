package chat.network.protobuffprotocol;

import chat.model.Message;
import chat.model.User;

import chat.services.ChatException;
import chat.services.IChatObserver;
import chat.services.IChatServices;

import java.io.*;
import java.net.Socket;


public class ProtoChatWorker implements Runnable, IChatObserver {
    private IChatServices server;
     private Socket connection;

     private InputStream input;
     private OutputStream output;
     private volatile boolean connected;
     public ProtoChatWorker(IChatServices server, Socket connection) {
         this.server = server;
         this.connection = connection;
         try{
             output=connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
             input=connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
             connected=true;
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public void run() {

         while(connected){
             try {
                // Object request=input.readObject();
                 System.out.println("Waiting requests ...");
                 ChatProtobufs.ChatRequest request=ChatProtobufs.ChatRequest.parseDelimitedFrom(input);
                 System.out.println("Request received: "+request);
                 ChatProtobufs.ChatResponse response=handleRequest(request);
                 if (response!=null){
                    sendResponse(response);
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
         try {
             input.close();
             output.close();
             connection.close();
         } catch (IOException e) {
             System.out.println("Error "+e);
         }
     }

     public void messageReceived(Message message) throws ChatException {
         System.out.println("Message received  "+message);
         try {
             sendResponse(ProtoUtils.createNewMessageResponse(message));
         } catch (IOException e) {
             throw new ChatException("Sending error: "+e);
         }
     }

     public void friendLoggedIn(User friend) throws ChatException {
         System.out.println("Friend logged in "+friend);
         try {
             sendResponse(ProtoUtils.createFriendLoggedInResponse(friend));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public void friendLoggedOut(User friend) throws ChatException {
         System.out.println("Friend logged out "+friend);
         try {
             sendResponse(ProtoUtils.createFriendLoggedOutResponse(friend));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     private ChatProtobufs.ChatResponse handleRequest(ChatProtobufs.ChatRequest request){
         ChatProtobufs.ChatResponse response=null;
         switch (request.getType()){
             case Login:{
                 System.out.println("Login request ...");
                 User user=ProtoUtils.getUser(request);
                 try {
                     server.login(user, this);
                     return ProtoUtils.createOkResponse();
                 } catch (ChatException e) {
                     connected=false;
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case Logout:{
                 System.out.println("Logout request");
                 User user=ProtoUtils.getUser(request);
                 try {
                     server.logout(user, this);
                     connected=false;
                     return ProtoUtils.createOkResponse();

                 } catch (ChatException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case SendMessage:{
                 System.out.println("SendMessageRequest ...");
                 Message message=ProtoUtils.getMessage(request);
                 try {
                     server.sendMessage(message);
                     return ProtoUtils.createOkResponse();
                 } catch (ChatException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case GetLoggedFriends:{
                 System.out.println("GetLoggedFriends Request ...");
                 User user=ProtoUtils.getUser(request);
                 try {
                     User[] friends=server.getLoggedFriends(user);
                     return ProtoUtils.createLoggedFriendsResponse(friends);
                 } catch (ChatException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
         }
         return response;
     }

     private void sendResponse(ChatProtobufs.ChatResponse response) throws IOException{
         System.out.println("sending response "+response);
         synchronized (output) {
             response.writeDelimitedTo(output);
             //output.writeObject(response);
             output.flush();
         }
     }
}
