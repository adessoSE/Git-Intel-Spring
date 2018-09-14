package de.adesso.gitstalker.core.exceptions;

/**
 * This Exception is used, if the created request isn't valid. This will cause the Github API to return null.
 */
public class InvalidRequestContentException extends Exception {

    public InvalidRequestContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestContentException(String message) {
        super(message);
    }
}
