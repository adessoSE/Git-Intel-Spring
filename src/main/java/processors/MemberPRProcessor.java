package processors;

import objects.Member.MemberPR;
import objects.Query;
import objects.ResponseWrapper;
import resources.memberPR_Resources.Members;
import resources.memberPR_Resources.NodesMember;
import resources.memberPR_Resources.NodesPR;

import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<String,ArrayList<String>> memberPRRepoIDs = new HashMap<>();
        Members members = this.requestQuery.getQueryResponse().getResponseMemberPR().getData().getOrganization().getMembers();
        for (NodesMember nodes : members.getNodes()) {
            for (NodesPR pullRequests : nodes.getPullRequests().getNodes()) {
                if(memberPRRepoIDs.containsKey(pullRequests.getRepository().getId())){
                    if(!memberPRRepoIDs.get(pullRequests.getRepository().getId()).contains(nodes.getId())){
                        memberPRRepoIDs.get(pullRequests.getRepository().getId()).add(nodes.getId());
                    }
                } else {
                    ArrayList<String> contributorIDs = new ArrayList<>();
                    contributorIDs.add(nodes.getId());
                    memberPRRepoIDs.put(pullRequests.getRepository().getId(),contributorIDs);
                }

            }
        }
        return new ResponseWrapper(new MemberPR(memberPRRepoIDs, members.getPageInfo().getEndCursor(), members.getPageInfo().isHasNextPage()));
    }
}
