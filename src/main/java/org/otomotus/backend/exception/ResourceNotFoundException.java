package org.otomotus.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wyjątek rzucany gdy szukany zasób nie zostanie znaleziony.
 * <p>
 * Automatycznie zwraca kod statusu HTTP 404 (Not Found).
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
