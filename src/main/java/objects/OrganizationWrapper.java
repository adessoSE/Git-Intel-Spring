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
    private ArrayList<String> memberIDs = new ArrayList<>();
    private Set<String> memberPRRepoIDs = new HashSet<>();

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

    public ArrayList<String> getMemberIDs() {
        return memberIDs;
    }

    public void setMemberIDs(ArrayList<String> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public void addMemberIDs(ArrayList<String> memberIDs) { this.memberIDs.addAll(memberIDs); }

    public Set<String> getMemberPRRepoIDs() {
        return memberPRRepoIDs;
    }

    public void setMemberPRRepoIDs(Set<String> memberPRRepoIDs) {
        this.memberPRRepoIDs = memberPRRepoIDs;
    }

    public void addMemberPRs(Set<String> memberPRRepoIDs) { this.memberPRRepoIDs.addAll(memberPRRepoIDs); }

}
