package objects;

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
    private ChartJSData internalRepositoriesCommits;
    private ChartJSData externalRepositoriesPullRequests;

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

    public ChartJSData getInternalRepositoriesCommits() {
        return internalRepositoriesCommits;
    }

    public void setInternalRepositoriesCommits(ChartJSData internalRepositoriesCommits) {
        this.internalRepositoriesCommits = internalRepositoriesCommits;
    }

    public void setNumOfExternalRepoContributions(int numOfExternalRepoContributions) {
        this.numOfExternalRepoContributions = numOfExternalRepoContributions;
    }

    public ChartJSData getExternalRepositoriesPullRequests() {
        return externalRepositoriesPullRequests;
    }

    public void setExternalRepositoriesPullRequests(ChartJSData externalRepositoriesPullRequests) {
        this.externalRepositoriesPullRequests = externalRepositoriesPullRequests;
    }
}
