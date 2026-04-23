package problema8.services.rest;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(Exception e) {
        super(e);
    }
}
