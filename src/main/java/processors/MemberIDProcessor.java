package processors;

import config.RateLimitConfig;
import objects.MemberID;
import objects.Query;
import objects.ResponseWrapper;
import resources.memberID_Resources.Members;
import resources.memberID_Resources.Nodes;
import resources.rateLimit_Resources.RateLimit;

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
        RateLimit rateLimit = this.requestQuery.getQueryResponse().getResponseMemberID().getData().getRateLimit();
        RateLimitConfig.setRemainingRateLimit(rateLimit.getRemaining());
        RateLimitConfig.setResetRateLimitAt(rateLimit.getResetAt());
        RateLimitConfig.addPreviousRequestCostAndRequestType(rateLimit.getCost(),requestQuery.getQueryRequestType());

        System.out.println("Rate Limit:"  + RateLimitConfig.getRateLimit());
        System.out.println("Rate Limit Remaining:"  + RateLimitConfig.getRemainingRateLimit());
        System.out.println("Request Cost:"  + RateLimitConfig.getPreviousRequestCostAndRequestType());
        System.out.println("Reset Rate Limit At: " + RateLimitConfig.getResetRateLimitAt());

        ArrayList<String> memberIDs = new ArrayList<>();
        Members members = this.requestQuery.getQueryResponse().getResponseMemberID().getData().getOrganization().getMembers();
        for (Nodes nodes : members.getNodes()) {
            memberIDs.add(nodes.getId());
        }
        return new ResponseWrapper(new MemberID(memberIDs, members.getPageInfo().getEndCursor(), members.getPageInfo().isHasNextPage()));
    }
}
