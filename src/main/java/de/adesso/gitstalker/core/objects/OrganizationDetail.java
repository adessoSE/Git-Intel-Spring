package de.adesso.gitstalker.core.objects;

import java.util.Date;
import lombok.Data;

@Data
public class OrganizationDetail {

    private String name;
    private String description;
    private String websiteURL;
    private String githubURL;
    private String location;
    private String avatarURL;
    private int numOfMembers;
    private int numOfTeams;
    private int numOfRepositories;
    private int numOfExternalRepoContributions;
    private int numOfCreatedReposByMembers;
    private ChartJSData internalRepositoriesCommits;
    private ChartJSData externalRepositoriesPullRequests;
    private Date lastUpdate;

    public OrganizationDetail(String name, String description, String websiteURL, String githubURL, String location, String avatarURL, int numOfMembers, int numOfRepositories, int numOfTeams) {
        this.setName(name);
        this.setDescription(description);
        this.setWebsiteURL(websiteURL);
        this.setGithubURL(githubURL);
        this.setLocation(location);
        this.setAvatarURL(avatarURL);
        this.setNumOfMembers(numOfMembers);
        this.setNumOfRepositories(numOfRepositories);
        this.setNumOfTeams(numOfTeams);
    }
}
