package de.adesso.gitstalker.core.resources.member_Resources;


public class User {

    private String name;
    private String id;
    private String login;
    private String url;
    private String avatarUrl;
    private RepositoriesContributedTo repositoriesContributedTo;
    private Issues issues;
    private PullRequests pullRequests;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getUrl() {
        return url;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public RepositoriesContributedTo getRepositoriesContributedTo() {
        return repositoriesContributedTo;
    }

    public Issues getIssues() {
        return issues;
    }

    public PullRequests getPullRequests() {
        return pullRequests;
    }




}
