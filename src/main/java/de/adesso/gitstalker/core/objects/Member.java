package de.adesso.gitstalker.core.objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Accessors(chain = true)
@Data
@NoArgsConstructor
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
}
