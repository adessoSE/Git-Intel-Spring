package de.adesso.gitstalker.core.objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class Repository {
    private String name;
    private String url;
    private String description;
    private String programmingLanguage;
    private String license;
    private int forks;
    private int stars;
    private ChartJSData previousCommits;
    private ChartJSData previousIssues;
    private ChartJSData previousPullRequests;
    private int amountPreviousCommits;
    private int amountPreviousIssues;
    private int amountPreviousPullRequests;
    private ArrayList<Member> contributors;

    public void addContributor(Member contributor) {
        this.contributors.add(contributor);
    }
}
