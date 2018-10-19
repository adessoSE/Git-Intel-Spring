package de.adesso.gitstalker.core.objects;

import lombok.Data;
import java.util.HashMap;

@Data
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
        this.setName(name);
        this.setUsername(username);
        this.setAvatarURL(avatarURL);
        this.setGithubURL(githubURL);
        this.setPreviousCommitsWithLink(previousCommitsWithLink);
        this.setPreviousIssuesWithLink(previousIssuesWithLink);
        this.setPreviousPullRequestsWithLink(previousPullRequestsWithLink);
        this.setAmountPreviousCommits(previousCommitsWithLink.size());
        this.setAmountPreviousIssues(previousIssuesWithLink.size());
        this.setAmountPreviousPullRequests(previousPullRequestsWithLink.size());
        this.setPreviousCommits(previousCommits);
        this.setPreviousIssues(previousIssues);
        this.setPreviousPullRequests(previousPullRequests);
    }
}
