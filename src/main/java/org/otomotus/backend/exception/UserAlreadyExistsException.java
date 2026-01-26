package org.otomotus.backend.exception;

/**
 * Wyjątek rzucany gdy użytkownik o podanym emailu lub nazwie użytkownika już istnieje.
 * <p>
 * Używany podczas rejestracji nowego użytkownika gdy dane nie są unikatowe.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Konstruktor z komunikatem błędu.
     *
     * @param message komunikat błędu
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String field, String value) {
        super(String.format("%s '%s' already exists", field, value));
    }
}
