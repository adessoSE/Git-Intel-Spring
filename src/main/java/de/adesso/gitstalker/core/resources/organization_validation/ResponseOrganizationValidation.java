package de.adesso.gitstalker.core.resources.organization_validation;

import de.adesso.gitstalker.core.objects.Response;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class ResponseOrganizationValidation implements Response {

    private Data data;
    private Errors errors;
}
