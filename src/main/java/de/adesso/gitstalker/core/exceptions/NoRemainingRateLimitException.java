package de.adesso.gitstalker.core.exceptions;

public class NoRemainingRateLimitException extends Exception {

    public NoRemainingRateLimitException(String message) {
        super(message);
    }

}
