package de.adesso.gitstalker.core.resources.externalRepo_Resources;

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

    public NodesRepositories() {}

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getForkCount() {
        return forkCount;
    }

    public Stargazers getStargazers() {
        return stargazers;
    }

    public LicenseInfo getLicenseInfo() {
        return licenseInfo;
    }

    public PrimaryLanguage getPrimaryLanguage() {
        return primaryLanguage;
    }

    public DefaultBranchRef getDefaultBranchRef() {
        return defaultBranchRef;
    }

    public PullRequests getPullRequests() {
        return pullRequests;
    }

    public Issues getIssues() {
        return issues;
    }

    public String getUrl() {
        return url;
    }
}
