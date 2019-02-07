package de.adesso.gitstalker.core.objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.Date;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@Component
public class OrganizationDetail {

    @Id
    private String id;

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
    private ChartJSData memberAmountHistory;
    private Date lastUpdate;
}
