package objects;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OrganizationWrapper {

    @Id
    private String id;

    private String organizationName;
    private OrganizationDetail organizationDetail;
    private Set<String> memberIDs = new HashSet<>();
    private Set<String> memberPRRepoIDs = new HashSet<>();
    private Set<String> organizationRepoIDs = new HashSet<>();

    /**
     * Wrapper for all the needed information of a organization.
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

    public Set<String> getMemberIDs() {
        return memberIDs;
    }

    public void setMemberIDs(Set<String> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public void addMemberIDs(Set<String> memberIDs) { this.memberIDs.addAll(memberIDs); }

    public Set<String> getMemberPRRepoIDs() {
        return memberPRRepoIDs;
    }

    public void setMemberPRRepoIDs(Set<String> memberPRRepoIDs) {
        this.memberPRRepoIDs = memberPRRepoIDs;
    }

    public void addMemberPRs(Set<String> memberPRRepoIDs) { this.memberPRRepoIDs.addAll(memberPRRepoIDs); }

    public Set<String> getOrganizationRepoIDs() {
        return organizationRepoIDs;
    }

    public void setOrganizationRepoIDs(Set<String> organizationRepoIDs) {
        this.organizationRepoIDs = organizationRepoIDs;
    }

    public void addOrganizationRepoIDs(Set<String> organizationRepoIDs) { this.organizationRepoIDs.addAll(organizationRepoIDs); }

}
