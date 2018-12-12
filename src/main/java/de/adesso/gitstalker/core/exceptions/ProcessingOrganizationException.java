package de.adesso.gitstalker.core.exceptions;

import lombok.Getter;

/**
 * This Exception is used, if the requested organization is still being processed.
 */
public class ProcessingOrganizationException extends Exception {

    @Getter
    String searchedOrganization;

    public ProcessingOrganizationException(String message, String searchedOrganization) {
        super(message);
        this.searchedOrganization = searchedOrganization;
    }
}
