package de.adesso.gitstalker.core.exceptions;

import lombok.Getter;

/**
 * This Exception is used, if the processed organization name is invalid.
 */
public class InvalidOrganizationNameRequestException extends Exception {

    @Getter
    String searchedOrganization;

    public InvalidOrganizationNameRequestException(String message, String searchedOrganization) {
        super(message);
        this.searchedOrganization = searchedOrganization;
    }
}
