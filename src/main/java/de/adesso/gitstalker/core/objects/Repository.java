package de.adesso.gitstalker.core.objects;

import java.util.ArrayList;

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
    //Optional for external Repos
    private ArrayList<Member> contributors;

    public Repository(){}

    public Repository(String name, String url, String description, String programmingLanguage, String license, int forks, int stars) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.programmingLanguage = programmingLanguage;
        this.license = license;
        this.forks = forks;
        this.stars = stars;
    }

    public Repository(String name, String url, String description, String programmingLanguage, String license, int forks, int stars, ChartJSData previousCommits, ChartJSData previousIssues, ChartJSData previousPullRequests) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.programmingLanguage = programmingLanguage;
        this.license = license;
        this.forks = forks;
        this.stars = stars;
        this.previousCommits = previousCommits;
        this.previousIssues = previousIssues;
        this.previousPullRequests = previousPullRequests;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public String getLicense() {
        return license;
    }

    public int getForks() {
        return forks;
    }

    public int getStars() {
        return stars;
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

    public String getUrl() {
        return url;
    }

    public ArrayList<Member> getContributor() {
        return contributors;
    }

    public void setContributor(ArrayList<Member> contributors) {
        this.contributors = contributors;
    }

    public void addContributor(Member contributor) {
        this.contributors.add(contributor);
    }

}
