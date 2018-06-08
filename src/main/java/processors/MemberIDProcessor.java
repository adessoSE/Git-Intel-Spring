package processors;

import objects.MemberID;
import objects.Query;
import objects.ResponseWrapper;
import resources.memberID_Resources.Members;
import resources.memberID_Resources.Nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MemberIDProcessor {

    private Query requestQuery;

    public MemberIDProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    /**
     * Response processing of the MemberID request. Processing through every MemberID and save it in a ArrayList.
     * Creating a MemberID object containing the MemberID ArrayList and the PageInfo wrapped into the ResponseWrapper.
     *
     * @return ResponseWrapper containing the MemberID object.
     */
    public ResponseWrapper processResponse() {
        ArrayList<String> memberIDs = new ArrayList<>();
        Members members = this.requestQuery.getQueryResponse().getResponseMemberID().getData().getOrganization().getMembers();
        for (Nodes nodes : members.getNodes()) {
            memberIDs.add(nodes.getId());
        }
        return new ResponseWrapper(new MemberID(memberIDs, members.getPageInfo().getEndCursor(), members.getPageInfo().isHasNextPage()));
    }
}
