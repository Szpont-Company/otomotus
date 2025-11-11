package org.otomotus.backend.exception;

public class UserAlreadyExistsException extends  RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String field, String value) {
        super(String.format("%s '%s' already exists", field, value));
    }
}
