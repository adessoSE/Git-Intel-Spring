package de.adesso.gitstalker.core.exceptions;

/**
 * This Exception is used, if the rate limit is exhausted.
 */
public class NoRemainingRateLimitException extends Exception {

    public NoRemainingRateLimitException(String message) {
        super(message);
    }

}
