package de.adesso.gitstalker.core.objects;

import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.parser.MemberGrowthChartJSParser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;

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
