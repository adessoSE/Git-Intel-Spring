package processors;

import objects.MemberID;
import objects.Query;
import objects.RepositoryID;
import objects.ResponseWrapper;

import java.util.ArrayList;

public class MemberPRProcessor {

    private Query requestQuery;

    public MemberPRProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<String> memberPRRepoIDs = new ArrayList<>();
        this.requestQuery.getQueryResponse().getOrganization().getMembers().getNodes().forEach(nodes -> nodes.getPullRequests().getNodes().forEach(pullRequests -> memberPRRepoIDs.add(pullRequests.getRepository().getId())));
        return new ResponseWrapper(new RepositoryID(memberPRRepoIDs,this.requestQuery.getQueryResponse().getOrganization().getMembers().getPageInfo().getEndCursor(),this.requestQuery.getQueryResponse().getOrganization().getMembers().getPageInfo().isHasNextPage()));
    }
}
