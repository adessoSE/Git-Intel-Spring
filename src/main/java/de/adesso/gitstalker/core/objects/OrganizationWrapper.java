package de.adesso.gitstalker.core.objects;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.*;

@Data
public class OrganizationWrapper {

    @Id
    private String id;

    private String organizationName;
    private OrganizationDetail organizationDetail;
    private ArrayList<String> memberIDs = new ArrayList<>();
    private HashMap<String, ArrayList<String>> memberPRRepoIDs = new HashMap<>();
    private HashMap<String, Member> members = new HashMap<>();
    private Set<RequestType> finishedRequests = new HashSet<>();
    private HashMap<String, Repository> repositories = new HashMap<>();
    private HashMap<String, Team> teams = new HashMap<>();
    private HashMap<String, Repository> externalRepos = new HashMap<>();
    private HashMap<String, ArrayList<Repository>> createdReposByMembers = new HashMap<>();
    private Date lastUpdateTimestamp;
    private Date lastAccessTimestamp;
    private int completeUpdateCost = 0;

    /**
     * Wrapper for all the needed information of a organization.
     *
     * @param organizationName Specified organizationName on GitHub
     */
    public OrganizationWrapper(String organizationName) {
        this.setOrganizationName(organizationName);
    }

    public void setCompleteUpdateCost(int completeUpdateCost) {
        this.completeUpdateCost += completeUpdateCost;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.organizationDetail.setLastUpdate(lastUpdateTimestamp);
    }

    public void addMemberIDs(ArrayList<String> memberIDs) {
        this.memberIDs.addAll(memberIDs);
    }

    public void addMemberPRs(HashMap<String, ArrayList<String>> memberPRRepoIDs) {
        this.memberPRRepoIDs.putAll(memberPRRepoIDs);
    }

    public void addMembers(HashMap<String, Member> members) {
        this.members.putAll(members);
    }

    public void addFinishedRequest(RequestType finishedRequest) {
        this.finishedRequests.add(finishedRequest);
    }

    public void addRepositories(HashMap<String, Repository> repositories) {
        this.repositories.putAll(repositories);
    }

    public void addTeams(HashMap<String,Team> teams) {
        this.teams.putAll(teams);
    }

    public void addExternalRepos(HashMap<String, Repository> externalRepos) {
        this.externalRepos.putAll(externalRepos);
    }

    public void addCreatedReposByMembers(HashMap<String, ArrayList<Repository>> createdReposByMembers) {
        this.createdReposByMembers.putAll(createdReposByMembers);
        int sumOfCreatedMemberRepos = 0;
        for(ArrayList<Repository> memberRepos : createdReposByMembers.values()){
            sumOfCreatedMemberRepos += memberRepos.size();
        }
        this.organizationDetail.setNumOfCreatedReposByMembers(sumOfCreatedMemberRepos);
    }

    public void prepareOrganizationForUpdateAndSaveIt(OrganizationRepository organizationRepository){
        this.organizationDetail = this.organizationDetail.resetOrganizationDetailWithoutDeletingMemberGrowthHistory();
        this.memberIDs = new ArrayList<>();
        this.memberPRRepoIDs = new HashMap<>();
        this.members = new HashMap<>();
        this.finishedRequests = new HashSet<>();
        this.repositories = new HashMap<>();
        this.teams = new HashMap<>();
        this.externalRepos = new HashMap<>();
        this.createdReposByMembers = new HashMap<>();
        this.completeUpdateCost = 0;
        this.lastUpdateTimestamp = null;

        organizationRepository.save(this);
    }
}
