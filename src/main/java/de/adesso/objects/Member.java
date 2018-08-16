package objects;

import java.util.Date;
import java.util.HashMap;

public class Member {

    private String name;
    private String username;
    private String avatarURL;
    private String githubURL;
    private int amountPreviousCommits;
    private int amountPreviousIssues;
    private int amountPreviousPullRequests;
    private HashMap<String, String> previousCommitsWithLink;
    private HashMap<String, String> previousIssuesWithLink;
    private HashMap<String, String> previousPullRequestsWithLink;
    private ChartJSData previousCommits;
    private ChartJSData previousIssues;
    private ChartJSData previousPullRequests;

    public Member(String name, String username, String avatarURL, String githubURL, HashMap<String, String> previousCommitsWithLink, HashMap<String, String> previousIssuesWithLink, HashMap<String, String> previousPullRequestsWithLink, ChartJSData previousCommits, ChartJSData previousIssues, ChartJSData previousPullRequests) {
        this.name = name;
        this.username = username;
        this.avatarURL = avatarURL;
        this.githubURL = githubURL;
        this.previousCommitsWithLink = previousCommitsWithLink;
        this.previousIssuesWithLink = previousIssuesWithLink;
        this.previousPullRequestsWithLink = previousPullRequestsWithLink;
        this.amountPreviousCommits = previousCommitsWithLink.size();
        this.amountPreviousIssues = previousIssuesWithLink.size();
        this.amountPreviousPullRequests = previousPullRequestsWithLink.size();
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

    public HashMap<String, String> getPreviousCommitsWithLink() {
        return previousCommitsWithLink;
    }

    public HashMap<String, String> getPreviousIssuesWithLink() {
        return previousIssuesWithLink;
    }

    public HashMap<String, String> getPreviousPullRequestsWithLink() {
        return previousPullRequestsWithLink;
    }
}
