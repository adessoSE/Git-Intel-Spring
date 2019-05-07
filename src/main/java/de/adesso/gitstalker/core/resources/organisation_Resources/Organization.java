package de.adesso.gitstalker.core.resources.organisation_Resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class Organization {

    private String name;
    private String location;
    private String websiteUrl;
    @JsonProperty("url")
    private String githubUrl;
    private String avatarUrl;
    private String description;
    private Members membersWithRole;
    private Teams teams;
    private Repositories repositories;
}
