package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Setter
@NoArgsConstructor
public class RequestManager {

    private String organizationName;
    private String endCursor = null;
    private String memberID;
    private List<String> repoIDs;

    public RequestManager setEndCursor(String endCursor) {
        this.endCursor = "\"" + endCursor + "\"";
        return this;
    }

    /**
     * Generation of the selected request. The request is returned as a query containing the necessary information.
     *
     * @param requestType Selection of the wanted request.
     * @return Generated query with the content of the selected query.
     */
    public Query generateRequest(RequestType requestType) {
        switch (requestType) {
            case ORGANIZATION_VALIDATION:
                return new OrganizationValidationRequest(organizationName).generateQuery();
            case ORGANIZATION_DETAIL:
                return new OrganizationDetailRequest(organizationName).generateQuery();
            case MEMBER_ID:
                return new MemberIDRequest(organizationName, endCursor).generateQuery();
            case MEMBER_PR:
                return new MemberPRRequest(organizationName, endCursor).generateQuery();
            case MEMBER:
                return new MemberRequest(organizationName, memberID).generateQuery();
            case REPOSITORY:
                return new RepositoryRequest(organizationName, endCursor).generateQuery();
            case TEAM:
                return new TeamRequest(organizationName, endCursor).generateQuery();
            case EXTERNAL_REPO:
                return new ExternalRepoRequest(organizationName, repoIDs).generateQuery();
            case CREATED_REPOS_BY_MEMBERS:
                return new CreatedReposByMembersRequest(organizationName, memberID, endCursor).generateQuery();
        }
        return null;
    }

    public ArrayList<Query> generateAllRequests() {
        ArrayList<RequestType> startRequests = new ArrayList<>();
        startRequests.add(RequestType.ORGANIZATION_DETAIL);
        startRequests.add(RequestType.REPOSITORY);
        startRequests.add(RequestType.MEMBER_ID);
        startRequests.add(RequestType.MEMBER_PR);
        startRequests.add(RequestType.TEAM);

        ArrayList<Query> allRequestQuerys = new ArrayList<>();
        for (RequestType startRequest : startRequests) {
            allRequestQuerys.add(this.generateRequest(startRequest));
        }

        return allRequestQuerys;
    }

    protected String formatInput(String input) {
        return input.replaceAll("\\s+", "").toLowerCase();
    }
}
