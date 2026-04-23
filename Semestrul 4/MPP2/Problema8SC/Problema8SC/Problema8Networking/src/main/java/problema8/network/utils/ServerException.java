package problema8.network.utils;

public class ServerException extends RuntimeException {
    public ServerException() {
        super();
    }
    public ServerException(String message) {
        super(message);
    }
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
