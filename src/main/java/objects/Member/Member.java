package objects.Member;

import objects.ChartJSData;

public class Member {

    private String name;
    private String username;
    private String avatarURL;
    private String githubURL;
    private ChartJSData previousCommits;
    private ChartJSData previousIssues;
    private ChartJSData previousPullRequests;

    /**
     * Representation of a member of Github.
     * @param name Defined name in bio
     * @param username Used login name (used at commits)
     * @param avatarURL URL to the used avatar
     * @param githubURL URL to the profile of the member on Github
     * @param previousCommits ChartJSData of the commits in the previous 7 days by the member
     * @param previousIssues ChartJSData of the issues in the previous 7 days by the member
     * @param previousPullRequests ChartJSData of the pull requests in the previous 7 days by the member
     */
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
