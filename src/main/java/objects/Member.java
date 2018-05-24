package objects;

public class Member {

    private String name;
    private String username;
    private String avatarURL;
    private String githubURL;
    private ChartJSData previousCommits;
    private ChartJSData previousIssues;
    private ChartJSData previousPullRequests;


    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public ChartJSData getPreviousCommits() {
        return previousCommits;
    }

    public ChartJSData getPreviousIssues() {
        return previousIssues;
    }

    public ChartJSData getPreviousPullRequests() {
        return previousPullRequests;
    }

}
