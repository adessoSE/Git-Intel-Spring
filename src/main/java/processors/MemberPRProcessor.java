package processors;

import objects.MemberPR;
import objects.Query;
import objects.ResponseWrapper;
import resources.memberPR_Resources.Members;
import resources.memberPR_Resources.NodesMember;
import resources.memberPR_Resources.NodesPR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        HashMap<String, ArrayList<String>> memberPRRepoIDs = new HashMap<>();
        Members members = this.requestQuery.getQueryResponse().getResponseMemberPR().getData().getOrganization().getMembers();
        for (NodesMember nodes : members.getNodes()) {
            for (NodesPR pullRequest : nodes.getPullRequests().getNodes()) {
                if (checkIfPullRequestIsActiveSinceOneYear(pullRequest.getUpdatedAt())) {
                    if (memberPRRepoIDs.containsKey(pullRequest.getRepository().getId())) {
                        if (!memberPRRepoIDs.get(pullRequest.getRepository().getId()).contains(nodes.getId())) {
                            memberPRRepoIDs.get(pullRequest.getRepository().getId()).add(nodes.getId());
                        }
                    } else {
                        ArrayList<String> contributorIDs = new ArrayList<>();
                        contributorIDs.add(nodes.getId());
                        memberPRRepoIDs.put(pullRequest.getRepository().getId(), contributorIDs);
                    }
                }
            }
        }
        return new ResponseWrapper(new MemberPR(memberPRRepoIDs, members.getPageInfo().getEndCursor(), members.getPageInfo().isHasNextPage()));
    }

    private boolean checkIfPullRequestIsActiveSinceOneYear(Date updatedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date oneYearAgo = calendar.getTime();

        return oneYearAgo.getTime() < updatedDate.getTime();
    }
}
