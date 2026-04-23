using chat.model;
using chat.networking.dto;

namespace chat.networking.jsonprotocol;

public class JsonProtocolUtils {
    public static Response createNewMessageResponse(Message message){
        return new Response{Type=ResponseType.NEW_MESSAGE, Message=DTOUtils.getDTO(message)};
    }

    public static Response createFriendLoggedInResponse(User friend){
        return new Response{Type =ResponseType.FRIEND_LOGGED_IN, User = DTOUtils.getDTO(friend)};
    }

    public static Response createFriendLoggedOutResponse(User friend){
        return new Response{Type=ResponseType.FRIEND_LOGGED_OUT, User=DTOUtils.getDTO(friend)};
    }

    public static Response createOkResponse(){
        return new Response{Type=ResponseType.OK};
    }

    public static Response createErrorResponse(String errorMessage){
        return new Response{Type=ResponseType.ERROR, ErrorMessage=errorMessage};
    }

    public static Response createGetLoggedFriendsResponse(User[] friends){
        return new Response{Type=ResponseType.GET_LOGGED_FRIENDS, Friends = DTOUtils.getDTO(friends)};
    }

    public static Request createLoginRequest(User user){
        return new Request{Type = RequestType.LOGIN, User = DTOUtils.getDTO(user)};
    }

    public static Request createSendMessageRequest(Message message){
        return new Request{Type=RequestType.SEND_MESSAGE, Message=DTOUtils.getDTO(message)};
    }

    public static Request createLogoutRequest(User user){
        return new Request{Type=RequestType.LOGOUT, User = DTOUtils.getDTO(user)};
    }

    public static Request createLoggedFriendsRequest(User user){
        return new Request{Type =RequestType.GET_LOGGED_FRIENDS, User = DTOUtils.getDTO(user)};
    }
}
