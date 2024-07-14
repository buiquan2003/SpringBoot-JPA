package jpa.spring.config.exception;

public class UserAccountExistingException extends RuntimeException {
    public UserAccountExistingException(String message) {
        super(message);
    }
}
