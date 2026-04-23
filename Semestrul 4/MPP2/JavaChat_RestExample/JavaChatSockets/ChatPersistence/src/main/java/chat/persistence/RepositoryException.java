package chat.persistence;

/**
 * Created by grigo on 5/11/17.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException() {
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
