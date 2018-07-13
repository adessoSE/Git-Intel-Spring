package objects.Organization;

import objects.ChartJSData;

public class OrganizationDetail {

    private String name;
    private String description;
    private String websiteURL;
    private String githubURL;
    private String location;
    private int numOfMembers;
    private int numOfTeams;
    private int numOfRepositories;
    private int numOfExternalRepoContributions;
    private ChartJSData previousCommits;
    private ChartJSData previousIssues;
    private ChartJSData previousPullRequests;

    /**
     * Wrapper-Object of the organization information.
     * Also contains ChartJSData of previousCommits, previousIssues, previousPullRequests. Information is generated in another query.
     * Also contains numOfExternalRepoContributions, which results out of the MemberPR query.
     * @param name Name of the organization
     * @param description Description of the organization
     * @param websiteURL URL linking to the website of the organization
     * @param githubURL URL linking to the Github profile of the organization
     * @param location Location of the organization
     * @param numOfMembers Total amount of members in the organization
     * @param numOfRepositories Total amount of repositories of the organization
     * @param numOfTeams Total amount of teams in the organization
     */
    public OrganizationDetail(String name, String description, String websiteURL, String githubURL, String location, int numOfMembers, int numOfRepositories, int numOfTeams) {
        this.name = name;
        this.description = description;
        this.websiteURL = websiteURL;
        this.githubURL = githubURL;
        this.location = location;
        this.numOfMembers = numOfMembers;
        this.numOfRepositories = numOfRepositories;
        this.numOfTeams = numOfTeams;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public String getLocation() {
        return location;
    }

    public int getNumOfMembers() {
        return numOfMembers;
    }

    public int getNumOfTeams() {
        return numOfTeams;
    }

    public int getNumOfRepositories() {
        return numOfRepositories;
    }

    public int getNumOfExternalRepoContributions() {
        return numOfExternalRepoContributions;
    }

    public ChartJSData getPreviousCommits() {
        return previousCommits;
    }

    public void setPreviousCommits(ChartJSData previousCommits) {
        this.previousCommits = previousCommits;
    }

    public ChartJSData getPreviousIssues() {
        return previousIssues;
    }

    public void setPreviousIssues(ChartJSData previousIssues) {
        this.previousIssues = previousIssues;
    }

    public ChartJSData getPreviousPullRequests() {
        return previousPullRequests;
    }

    public void setPreviousPullRequests(ChartJSData previousPullRequests) {
        this.previousPullRequests = previousPullRequests;
    }

    public void setNumOfExternalRepoContributions(int numOfExternalRepoContributions) {
        this.numOfExternalRepoContributions = numOfExternalRepoContributions;
    }
}
