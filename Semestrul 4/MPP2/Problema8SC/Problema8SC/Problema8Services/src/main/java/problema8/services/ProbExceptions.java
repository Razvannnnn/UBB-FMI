package problema8.services;

public class ProbExceptions extends RuntimeException {
    public ProbExceptions() {
    }
    public ProbExceptions(String message) {
        super(message);
    }
    public ProbExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
