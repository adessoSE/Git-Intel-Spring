package processors;

import objects.MemberID;
import objects.Query;
import objects.ResponseWrapper;

import java.util.ArrayList;

public class MemberIDProcessor {

    private Query requestQuery;

    public MemberIDProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<String> memberIDs = new ArrayList<>();
        this.requestQuery.getQueryResponse().getOrganization().getMembers().getNodes().forEach(nodes -> memberIDs.add(nodes.getId()) );
        return new ResponseWrapper(new MemberID(memberIDs,this.requestQuery.getQueryResponse().getOrganization().getMembers().getPageInfo().getEndCursor(),this.requestQuery.getQueryResponse().getOrganization().getMembers().getPageInfo().isHasNextPage()));
    }
}
