package de.adesso.gitstalker.core.objects;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import de.adesso.gitstalker.core.config.Config;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Accessors(chain = true)
@Data
@NoArgsConstructor
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
    private LinkedHashMap<String, Integer> memberAmountHistory;
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

    public OrganizationDetail addMemberAmountHistory(int currentMemberAmount) {
        if (Objects.isNull(this.memberAmountHistory)){
            this.memberAmountHistory = new LinkedHashMap<>();
        }
        this.memberAmountHistory.put(new Date().toString(), currentMemberAmount);
        this.adaptMemberHistoryToConfiguratedTimePeriod(memberAmountHistory);
        return this;
    }

    private LinkedHashMap<String, Integer> adaptMemberHistoryToConfiguratedTimePeriod(LinkedHashMap<String, Integer> memberAmountHistory){
        if (memberAmountHistory.size() > Config.PAST_DAYS_AMOUNT_TO_CRAWL){
            String firstKey = memberAmountHistory.keySet().stream().findFirst().get();
            memberAmountHistory.remove(firstKey);
        }
        return memberAmountHistory;
    }

    public OrganizationDetail resetOrganizationDetailWithoutDeletingMemberGrowthHistory(){
        OrganizationDetail resetedOrganizationDetail = new OrganizationDetail();
        resetedOrganizationDetail.setMemberAmountHistory(this.memberAmountHistory);
        return resetedOrganizationDetail;
    }
}
