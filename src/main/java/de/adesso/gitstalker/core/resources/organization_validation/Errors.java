package de.adesso.gitstalker.core.resources.organization_validation;

import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class Errors {

    private String message;
    private String type;
}
