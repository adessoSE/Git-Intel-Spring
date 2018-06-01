package objects;

public class Member {

    private String name;
    private String username;
    private String avatarURL;
    private String githubURL;
    private ChartJSData previousCommits;
    private ChartJSData previousIssues;
    private ChartJSData previousPullRequests;

    public Member(String name, String username, String avatarURL, String githubURL, ChartJSData previousCommits, ChartJSData previousIssues, ChartJSData previousPullRequests) {
        this.name = name;
        this.username = username;
        this.avatarURL = avatarURL;
        this.githubURL = githubURL;
        this.previousCommits = previousCommits;
        this.previousIssues = previousIssues;
        this.previousPullRequests = previousPullRequests;
    }

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
