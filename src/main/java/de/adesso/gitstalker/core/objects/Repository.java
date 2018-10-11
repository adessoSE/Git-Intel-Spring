package de.adesso.gitstalker.core.objects;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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
    private ArrayList<Member> contributors;

    public Repository(String name, String url, String description, String programmingLanguage, String license, int forks, int stars) {
        this.setName(name);
        this.setUrl(url);
        this.setDescription(description);
        this.setProgrammingLanguage(programmingLanguage);
        this.setLicense(license);
        this.setForks(forks);
        this.setStars(stars);
    }

    public Repository(String name, String url, String description, String programmingLanguage, String license, int forks, int stars, ChartJSData previousCommits, ChartJSData previousIssues, ChartJSData previousPullRequests) {
        this.setName(name);
        this.setUrl(url);
        this.setDescription(description);
        this.setProgrammingLanguage(programmingLanguage);
        this.setLicense(license);
        this.setForks(forks);
        this.setStars(stars);
        this.setPreviousCommits(previousCommits);
        this.setPreviousIssues(previousIssues);
        this.setPreviousPullRequests(previousPullRequests);
    }

    public void addContributor(Member contributor) {
        this.contributors.add(contributor);
    }

}
