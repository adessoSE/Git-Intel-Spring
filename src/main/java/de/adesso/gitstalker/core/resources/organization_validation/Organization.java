package de.adesso.gitstalker.core.resources.organization_validation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Organization {

    private String name;
    private Members membersWithRole;
    private Repositories repositories;
    private Teams teams;
}
