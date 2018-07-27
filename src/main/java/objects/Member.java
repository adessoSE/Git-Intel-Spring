package objects;

public class Member {

    private String name;
    private String username;
    private String avatarURL;
    private String githubURL;
    private int amountPreviousCommits;
    private int amountPreviousIssues;
    private int amountPreviousPullRequests;
    private ChartJSData previousCommits;
    private ChartJSData previousIssues;
    private ChartJSData previousPullRequests;

    public Member(String name, String username, String avatarURL, String githubURL, int amountPreviousCommits, int amountPreviousIssues, int amountPreviousPullRequests, ChartJSData previousCommits, ChartJSData previousIssues, ChartJSData previousPullRequests) {
        this.name = name;
        this.username = username;
        this.avatarURL = avatarURL;
        this.githubURL = githubURL;
        this.amountPreviousCommits = amountPreviousCommits;
        this.amountPreviousIssues = amountPreviousIssues;
        this.amountPreviousPullRequests = amountPreviousPullRequests;
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

    public int getAmountPreviousCommits() {
        return amountPreviousCommits;
    }

    public int getAmountPreviousIssues() {
        return amountPreviousIssues;
    }

    public int getAmountPreviousPullRequests() {
        return amountPreviousPullRequests;
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
