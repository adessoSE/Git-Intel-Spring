package requests;

import enums.RequestType;
import objects.Query;
import org.springframework.beans.factory.annotation.Autowired;
import repositories.RequestRepository;

import java.util.List;

public class RequestManager {

    private String organizationName;
    private String endCursor = null;
    private List<String> memberIDs;

    @Autowired
    RequestRepository requestRepository;

    /**
     * Declaration of different constructors to fit different request structures.
     */
    public RequestManager(String organizationName) {
        this.organizationName = organizationName;
    }

    public RequestManager(String organizationName, String endCursor) {
        this.organizationName = organizationName;
        this.endCursor = "\"" + endCursor + "\"";
    }

    public RequestManager(String organizationName, List<String> memberIDs) {
        this.organizationName = organizationName;
        this.memberIDs = memberIDs;
    }

    /**
     * Generation of the selected request. The request is returned as a query containing the necessary information.
     *
     * @param requestType Selection of the wanted request.
     * @return Generated query with the content of the selected query.
     */
    public Query generateRequest(RequestType requestType) {
        switch (requestType) {
            case ORGANIZATION_DETAIL:
                return new OrganizationDetailRequest(organizationName).generateQuery();
            case MEMBER_ID:
                return new MemberIDRequest(organizationName, endCursor).generateQuery();
            case MEMBER_PR:
                return new MemberPRRequest(organizationName, endCursor).generateQuery();
            case REPOSITORY_ID:
                return new RepositoryIDRequest(organizationName, endCursor).generateQuery();
            case MEMBER:
                return new MemberRequest(organizationName, memberIDs).generateQuery();
            case REPOSITORY:
                return new RepositoryRequest(organizationName, endCursor).generateQuery();
            case TEAM:
                return new TeamRequest(organizationName,endCursor).generateQuery();
            default:
                return null;
        }
    }
}
