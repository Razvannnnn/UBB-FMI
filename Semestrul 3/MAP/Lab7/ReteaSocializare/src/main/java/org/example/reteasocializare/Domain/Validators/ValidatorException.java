package org.example.reteasocializare.Domain.Validators;

public class ValidatorException extends RuntimeException {
    public ValidatorException(String message) {
      super(message);
    }

    public ValidatorException(String message, Throwable cause) {
      super(message, cause);
    }

    public ValidatorException(Throwable cause) {
      super(cause);
    }

    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }

    public ValidatorException() {
        super();
    }
}
