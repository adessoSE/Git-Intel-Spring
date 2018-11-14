package de.adesso.gitstalker.core.exceptions;

import lombok.Getter;

public class InvalidOrganizationNameRequestException extends Exception {

    @Getter
    String searchedOrganization;

    public InvalidOrganizationNameRequestException(String message, String searchedOrganization) {
        super(message);
        this.searchedOrganization = searchedOrganization;
    }
}
