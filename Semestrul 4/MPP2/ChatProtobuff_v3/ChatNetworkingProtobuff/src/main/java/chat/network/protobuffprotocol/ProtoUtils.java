package chat.network.protobuffprotocol;

import chat.model.Message;
import chat.model.User;


public class ProtoUtils {
    public static ChatProtobufs.ChatRequest createLoginRequest(User user){
        ChatProtobufs.User userDTO=ChatProtobufs.User.newBuilder().setId(user.getId()).setPasswd(user.getPasswd()).build();
        ChatProtobufs.ChatRequest request= ChatProtobufs.ChatRequest.newBuilder().setType(ChatProtobufs.ChatRequest.Type.Login)
                .setUser(userDTO).build();
        return request;
    }
    public static ChatProtobufs.ChatRequest createLogoutRequest(User user){
        ChatProtobufs.User userDTO=ChatProtobufs.User.newBuilder().setId(user.getId()).build();
        ChatProtobufs.ChatRequest request= ChatProtobufs.ChatRequest.newBuilder().setType(ChatProtobufs.ChatRequest.Type.Logout)
                .setUser(userDTO).build();
        return request;
    }

    public static ChatProtobufs.ChatRequest createSendMesssageRequest(Message message){
        ChatProtobufs.Message messageDTO=ChatProtobufs.Message.newBuilder().
                setSenderId(message.getSender().getId())
                .setReceiverId(message.getReceiver().getId())
                .setText(message.getText()).build();
        ChatProtobufs.ChatRequest request= ChatProtobufs.ChatRequest.newBuilder()
                .setType(ChatProtobufs.ChatRequest.Type.SendMessage)
                .setMessage(messageDTO).build();
        return request;
    }

    public static ChatProtobufs.ChatRequest createLoggedFriendsRequest(User user){
        ChatProtobufs.User userDTO=ChatProtobufs.User.newBuilder().setId(user.getId()).build();
        ChatProtobufs.ChatRequest request= ChatProtobufs.ChatRequest.newBuilder()
                .setType(ChatProtobufs.ChatRequest.Type.GetLoggedFriends)
                .setUser(userDTO).build();
        return request;
    }


    public static ChatProtobufs.ChatResponse createOkResponse(){
        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.Ok).build();
        return response;
    }

    public static ChatProtobufs.ChatResponse createErrorResponse(String text){
        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.Error)
                .setError(text).build();
        return response;
    }

    public static ChatProtobufs.ChatResponse createFriendLoggedInResponse(User user){
        ChatProtobufs.User userDTO=ChatProtobufs.User.newBuilder().setId(user.getId()).build();

        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.FriendLoggedIn)
                .setUser(userDTO).build();
        return response;
    }

    public static ChatProtobufs.ChatResponse createFriendLoggedOutResponse(User user){
        ChatProtobufs.User userDTO=ChatProtobufs.User.newBuilder().setId(user.getId()).build();

        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.FriendLoggedOut)
                .setUser(userDTO).build();
        return response;
    }
    public static ChatProtobufs.ChatResponse createNewMessageResponse(Message message){
        ChatProtobufs.Message messageDTO=ChatProtobufs.Message.newBuilder()
                .setSenderId(message.getSender().getId())
                .setReceiverId(message.getReceiver().getId())
                .setText(message.getText())
                .build();

        ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.NewMessage)
                .setMessage(messageDTO).build();
        return response;
    }

    public static ChatProtobufs.ChatResponse createLoggedFriendsResponse(User[] users){
        ChatProtobufs.ChatResponse.Builder response=ChatProtobufs.ChatResponse.newBuilder()
                .setType(ChatProtobufs.ChatResponse.Type.GetLoggedFriends);
        for (User user: users){
            ChatProtobufs.User userDTO=ChatProtobufs.User.newBuilder().setId(user.getId()).build();
              response.addFriends(userDTO);
        }

        return response.build();
    }

    public static String getError(ChatProtobufs.ChatResponse response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static User getUser(ChatProtobufs.ChatResponse response){
        User user=new User();
        user.setId(response.getUser().getId());
        return user;
    }

    public static Message getMessage(ChatProtobufs.ChatResponse response){
        User sender=new User();
        sender.setId(response.getMessage().getSenderId());
        User receiver=new User();
        receiver.setId(response.getMessage().getReceiverId());
        Message message=new Message(sender,response.getMessage().getText(), receiver);
        return message;
    }

    public static User[] getFriends(ChatProtobufs.ChatResponse response){
        User[] friends=new User[response.getFriendsCount()];
        for(int i=0;i<response.getFriendsCount();i++){
            ChatProtobufs.User userDTO=response.getFriends(i);
            User user=new User();
            user.setId(userDTO.getId());
            friends[i]=user;
        }
        return friends;
    }
    public static User getUser(ChatProtobufs.ChatRequest request){
        User user=new User();
        user.setId(request.getUser().getId());
        user.setPasswd(request.getUser().getPasswd());
        return user;
    }

    public static Message getMessage(ChatProtobufs.ChatRequest request){
            User sender=new User();
            sender.setId(request.getMessage().getSenderId());
            User receiver=new User();
            receiver.setId(request.getMessage().getReceiverId());
            Message message=new Message(sender,request.getMessage().getText(), receiver);
            return message;
        }

}
