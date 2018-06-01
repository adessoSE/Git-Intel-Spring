package resources.member_Resources;


public class NodesMember {

    private String name;
    private String login;
    private String url;
    private String avatarUrl;
    private RepositoriesContributedTo repositoriesContributedTo;
    private Issues issues;
    private PullRequests pullRequests;

    public NodesMember() {
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
