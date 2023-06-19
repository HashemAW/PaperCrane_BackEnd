package ca.papercrane.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The default exception sent when the database is queried for a resource that does not exist.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new {@link ResourceNotFoundException}
     *
     * @param message The message sent when the exception occurs.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

}