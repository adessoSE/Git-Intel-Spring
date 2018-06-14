package objects;

import enums.RequestType;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OrganizationWrapper {

    @Id
    private String id;

    private String organizationName;
    private OrganizationDetail organizationDetail;
    private ArrayList<String> memberIDs = new ArrayList<>();
    private Set<String> memberPRRepoIDs = new HashSet<>();
    private ArrayList<String> organizationRepoIDs = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<RequestType> finishedRequests = new ArrayList<>();
    private ArrayList<Repository> repositories = new ArrayList<>();
    private ArrayList<Integer> memberAmount = new ArrayList<>();

    /**
     * Wrapper for all the needed information of a organization.
     *
     * @param organizationName Specified organizationName on GitHub
     */
    public OrganizationWrapper(String organizationName) {
        this.organizationName = organizationName;
    }


    public String getId() {
        return id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public OrganizationDetail getOrganizationDetail() {
        return organizationDetail;
    }

    public void setOrganizationDetail(OrganizationDetail organizationDetail) {
        this.organizationDetail = organizationDetail;
    }

    public ArrayList<String> getMemberIDs() {
        return memberIDs;
    }

    public void setMemberIDs(ArrayList<String> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public void addMemberIDs(ArrayList<String> memberIDs) {
        this.memberIDs.addAll(memberIDs);
    }

    public Set<String> getMemberPRRepoIDs() {
        return memberPRRepoIDs;
    }

    public void setMemberPRRepoIDs(Set<String> memberPRRepoIDs) {
        this.memberPRRepoIDs = memberPRRepoIDs;
    }

    public void addMemberPRs(Set<String> memberPRRepoIDs) {
        this.memberPRRepoIDs.addAll(memberPRRepoIDs);
    }

    public ArrayList<String> getOrganizationRepoIDs() {
        return organizationRepoIDs;
    }

    public void setOrganizationRepoIDs(ArrayList<String> organizationRepoIDs) {
        this.organizationRepoIDs = organizationRepoIDs;
    }

    public void addOrganizationRepoIDs(ArrayList<String> organizationRepoIDs) {
        this.organizationRepoIDs.addAll(organizationRepoIDs);
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public void addMembers(ArrayList<Member> members) {
        this.members.addAll(members);
    }

    public ArrayList<RequestType> getFinishedRequests() {
        return finishedRequests;
    }

    public void addFinishedRequest(RequestType finishedRequest) { this.finishedRequests.add(finishedRequest); }

    public void setRepositories(ArrayList<Repository> repositories) {
        this.repositories = repositories;
    }

    public void addRepositories(ArrayList<Repository> repositories) {
        this.repositories.addAll(repositories);
    }

    public ArrayList<Integer> getMemberAmount() {
        return memberAmount;
    }

    public void addMemberAmount(Integer memberAmount) {
        this.memberAmount.add(memberAmount);
    }
}
