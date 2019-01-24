package de.adesso.gitstalker.core.exceptions;

import lombok.Getter;

/**
 * This Exception is used, if the rate limit is exhausted.
 */
public class NoRemainingRateLimitException extends Exception {

    @Getter
    String searchedOrganization;

    public NoRemainingRateLimitException(String message) {
        super(message);
    }

    public NoRemainingRateLimitException(String message, String searchedOrganization) {
        super(message);
        this.searchedOrganization = searchedOrganization;
    }

}
