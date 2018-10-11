package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
import de.adesso.gitstalker.core.resources.memberID_Resources.Members;
import de.adesso.gitstalker.core.resources.memberID_Resources.Nodes;
import de.adesso.gitstalker.core.resources.memberID_Resources.PageInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class MemberIDProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

    private ArrayList<String> memberIDs = new ArrayList<>();

    protected void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.organization = this.organizationRepository.findByOrganizationName(requestQuery.getOrganizationName());
    }

    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository);
        super.updateRateLimit(this.requestQuery.getQueryResponse().getResponseMemberID().getData().getRateLimit(), requestQuery.getQueryRequestType());

        this.processQueryResponse(this.requestQuery.getQueryResponse().getResponseMemberID().getData().getOrganization().getMembers());
        this.processRequestForRemainingInformation(this.requestQuery.getQueryResponse().getResponseMemberID().getData().getOrganization().getMembers().getPageInfo(), this.requestQuery.getOrganizationName());
        super.doFinishingQueryProcedure(requestRepository, organizationRepository, organization, requestQuery, RequestType.MEMBER_ID);
    }

    protected void processRequestForRemainingInformation(PageInfo pageInfo, String organizationName) {
        if (pageInfo.isHasNextPage()) {
            super.generateNextRequests(organizationName, pageInfo.getEndCursor(), RequestType.MEMBER_ID, requestRepository);
        } else {
            this.organization.addMemberIDs(this.memberIDs);
            this.generateNextRequestsBasedOnMemberIDs(this.memberIDs);
        }
    }

    protected void generateNextRequestsBasedOnMemberIDs(ArrayList<String> memberIDs) {
        for (String memberID : memberIDs) {
            requestRepository.save(new RequestManager(this.requestQuery.getOrganizationName(), memberID, RequestType.MEMBER).generateRequest(RequestType.MEMBER));
            requestRepository.save(new RequestManager(this.requestQuery.getOrganizationName(), memberID, RequestType.CREATED_REPOS_BY_MEMBERS).generateRequest(RequestType.CREATED_REPOS_BY_MEMBERS));
        }
    }

    protected void processQueryResponse(Members members) {
        for (Nodes nodes : members.getNodes()) {
            this.memberIDs.add(nodes.getId());
        }
    }
}
