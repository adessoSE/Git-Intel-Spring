package processors;

import objects.*;
import resources.memberPR_Resources.Organization;

import java.util.ArrayList;

public class MemberPRProcessor {

    private Query requestQuery;

    public MemberPRProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<String> memberPRRepoIDs = new ArrayList<>();
        Organization organization = this.requestQuery.getQueryResponse().getResponseMemberPR().getData().getOrganization();
        System.out.println(organization.getMembers().getPageInfo().getEndCursor());
        organization.getMembers().getNodes().forEach(nodes -> nodes.getPullRequests().getNodes().forEach(pullRequests -> memberPRRepoIDs.add(pullRequests.getRepository().getId())));
        System.out.println(memberPRRepoIDs.size());
        return new ResponseWrapper(new MemberPR(memberPRRepoIDs,organization.getMembers().getPageInfo().getEndCursor(),organization.getMembers().getPageInfo().isHasNextPage()));
    }
}
