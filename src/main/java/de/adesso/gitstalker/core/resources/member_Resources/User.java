package de.adesso.gitstalker.core.resources.member_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private String name;
    private String id;
    private String login;
    private String url;
    private String avatarUrl;
    private RepositoriesContributedTo repositoriesContributedTo;
    private Issues issues;
    private PullRequests pullRequests;
}
