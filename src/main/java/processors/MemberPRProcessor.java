package processors;

import objects.MemberPR;
import objects.Query;
import objects.ResponseWrapper;
import resources.memberPR_Resources.Members;
import resources.memberPR_Resources.NodesMember;
import resources.memberPR_Resources.NodesPR;

import java.util.ArrayList;

public class MemberPRProcessor {

    private Query requestQuery;

    public MemberPRProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    /**
     * Response processing of the MemberPR request. Processing through every MemberPRRepoID and save it in a ArrayList.
     * Creating a MemberPR object containing the MemberPRRepoID ArrayList and the PageInfo wrapped into the ResponseWrapper.
     *
     * @return ResponseWrapper containing the MemberPR object.
     */
    public ResponseWrapper processResponse() {
        ArrayList<String> memberPRRepoIDs = new ArrayList<>();
        Members members = this.requestQuery.getQueryResponse().getResponseMemberPR().getData().getOrganization().getMembers();
        for (NodesMember nodes : members.getNodes()) {
            for (NodesPR pullRequests : nodes.getPullRequests().getNodes()) {
                memberPRRepoIDs.add(pullRequests.getRepository().getId());
            }
        }
        return new ResponseWrapper(new MemberPR(memberPRRepoIDs, members.getPageInfo().getEndCursor(), members.getPageInfo().isHasNextPage()));
    }
}
