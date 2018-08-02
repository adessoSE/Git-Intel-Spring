package processors;

import config.Config;
import objects.MemberPR;
import objects.Query;
import objects.ResponseWrapper;
import resources.memberPR_Resources.Members;
import resources.memberPR_Resources.NodesMember;
import resources.memberPR_Resources.NodesPR;

import java.util.*;

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
        HashMap<String,ArrayList<Date>> pullRequestsDates = new HashMap<>();
        Members members = this.requestQuery.getQueryResponse().getResponseMemberPR().getData().getOrganization().getMembers();
        for (NodesMember nodes : members.getNodes()) {
            for (NodesPR pullRequests : nodes.getPullRequests().getNodes()) {
                if(memberPRRepoIDs.containsKey(pullRequests.getRepository().getId())){
                    //TODO: Change to Set!
                    if(!memberPRRepoIDs.get(pullRequests.getRepository().getId()).contains(nodes.getId())){
                        memberPRRepoIDs.get(pullRequests.getRepository().getId()).add(nodes.getId());
                    }
                    if (new Date(System.currentTimeMillis() - (Config.PAST_DAYS_AMOUNT_TO_CRAWL * 1000 * 60 * 60 * 24)).getTime() < pullRequests.getUpdatedAt().getTime()) {
                        if(pullRequestsDates.containsKey(pullRequests.getRepository().getId())){
                            pullRequestsDates.get(pullRequests.getRepository().getId()).add(pullRequests.getUpdatedAt());
                        } else pullRequestsDates.put(pullRequests.getRepository().getId(),new ArrayList<>(Arrays.asList(pullRequests.getUpdatedAt())));
                    }


                } else {
                    ArrayList<String> contributorIDs = new ArrayList<>();
                    contributorIDs.add(nodes.getId());
                    memberPRRepoIDs.put(pullRequests.getRepository().getId(),contributorIDs);
                }
            }
        }
        return new ResponseWrapper(new MemberPR(memberPRRepoIDs, members.getPageInfo().getEndCursor(), members.getPageInfo().isHasNextPage()), pullRequestsDates);
    }

    private boolean checkIfPullRequestIsActiveSinceOneYear(Date updatedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date oneYearAgo = calendar.getTime();

        return oneYearAgo.getTime() < updatedDate.getTime();
    }
}
