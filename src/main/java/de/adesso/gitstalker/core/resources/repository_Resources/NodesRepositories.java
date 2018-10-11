package de.adesso.gitstalker.core.resources.repository_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NodesRepositories {

    private String name;
    private String id;
    private String url;
    private String description;
    private int forkCount;
    private Stargazers stargazers;
    private LicenseInfo licenseInfo;
    private PrimaryLanguage primaryLanguage;
    private DefaultBranchRef defaultBranchRef;
    private PullRequests pullRequests;
    private Issues issues;
}
