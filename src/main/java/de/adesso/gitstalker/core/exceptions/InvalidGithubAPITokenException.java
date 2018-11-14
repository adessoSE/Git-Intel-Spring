package de.adesso.gitstalker.core.exceptions;

import lombok.Getter;

/**
 * This Exception is used, if the Github API Token is invalid. The token can be invalid because of never been set, suspended of Github or deleted in Github Account.
 */
public class InvalidGithubAPITokenException extends Exception {

    @Getter
    String searchedOrganization;

    public InvalidGithubAPITokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGithubAPITokenException(String message, String searchedOrganization) {
        super(message);
        this.searchedOrganization = searchedOrganization;
    }
}
