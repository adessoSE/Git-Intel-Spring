package de.adesso.gitstalker.core.exceptions;

/**
 * This Exception is used, if the Github API Token is invalid. The token can be invalid because of never been set, suspended of Github or deleted in Github Account.
 */
public class InvalidGithubAPITokenException extends Exception {

    public InvalidGithubAPITokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
