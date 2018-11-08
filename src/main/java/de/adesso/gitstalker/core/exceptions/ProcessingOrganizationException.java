package de.adesso.gitstalker.core.exceptions;

import lombok.Getter;

public class ProcessingOrganizationException extends Exception {

    @Getter
    String searchedOrganization;

    public ProcessingOrganizationException(String message, String searchedOrganization) {
        super(message);
        this.searchedOrganization = searchedOrganization;
    }
}
