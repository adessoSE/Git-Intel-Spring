package de.adesso.gitstalker.core.resources.team_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NodesTeams {

    private String name;
    private String id;
    private String description;
    private String avatarUrl;
    private String url;
    private Repositories repositories;
    private Members members;
}
