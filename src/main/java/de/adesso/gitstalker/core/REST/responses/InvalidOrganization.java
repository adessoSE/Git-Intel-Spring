package de.adesso.gitstalker.core.REST.responses;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class InvalidOrganization {
    private String errorMessage;
    private String searchedOrganization;
}
