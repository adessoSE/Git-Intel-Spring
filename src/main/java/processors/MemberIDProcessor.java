package processors;

import objects.MemberID;
import objects.Query;
import objects.ResponseWrapper;
import resources.memberID_Resources.Organization;

import java.util.ArrayList;

public class MemberIDProcessor {

    private Query requestQuery;

    public MemberIDProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<String> memberIDs = new ArrayList<>();
        Organization organization = this.requestQuery.getQueryResponse().getResponseMemberID().getData().getOrganization();
        organization.getMembers().getNodes().forEach(nodes -> memberIDs.add(nodes.getId()));
        return new ResponseWrapper(new MemberID(memberIDs, organization.getMembers().getPageInfo().getEndCursor(), organization.getMembers().getPageInfo().isHasNextPage()));
    }
}
