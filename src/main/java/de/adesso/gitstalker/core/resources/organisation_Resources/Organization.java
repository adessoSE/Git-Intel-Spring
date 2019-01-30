package de.adesso.gitstalker.core.resources.organisation_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Organization {

    private String name;
    private String location;
    private String websiteUrl;
    private String githubUrl;
    private String avatarUrl;
    private String description;
    private Members members;
    private Teams teams;
    private Repositories repositories;
}
