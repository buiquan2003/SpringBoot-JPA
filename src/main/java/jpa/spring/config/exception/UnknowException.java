package jpa.spring.config.exception;

public class UnknowException extends RuntimeException {
    public UnknowException(String message) {
        super(message);
    }
}
