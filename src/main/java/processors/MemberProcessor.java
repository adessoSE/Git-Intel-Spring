package processors;

import config.Config;
import objects.Member;
import objects.Query;
import objects.ResponseWrapper;
import resources.member_Resources.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class MemberProcessor extends ResponseProcessor {

    private Query requestQuery;

    public MemberProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        super.updateRateLimit(this.requestQuery.getQueryResponse().getResponseMember().getData().getRateLimit(), requestQuery.getQueryRequestType());

        HashMap<String, Member> members = new HashMap<>();
        User singleMember = this.requestQuery.getQueryResponse().getResponseMember().getData().getNode();

        ArrayList<Date> pullRequestDates = new ArrayList<>();
        ArrayList<Date> issuesDates = new ArrayList<>();
        ArrayList<Date> commitsDates = new ArrayList<>();

        int amountPreviousCommits;
        int amountPreviousIssues;
        int amountPreviousPRs;

        HashMap<String, ArrayList<Date>> committedRepos = new HashMap<>();

        for (NodesPullRequests nodesPullRequests : singleMember.getPullRequests().getNodes()) {
            if (new Date(System.currentTimeMillis() - Config.PAST_DAYS_TO_CRAWL_IN_MS).getTime() < nodesPullRequests.getCreatedAt().getTime()) {

                pullRequestDates.add(nodesPullRequests.getCreatedAt());
            }
        }
        for (NodesIssues nodesIssues : singleMember.getIssues().getNodes()) {
            if (new Date(System.currentTimeMillis() - Config.PAST_DAYS_TO_CRAWL_IN_MS).getTime() < nodesIssues.getCreatedAt().getTime()) {
                issuesDates.add(nodesIssues.getCreatedAt());
            }
        }
        for (NodesRepoContributedTo nodesRepoContributedTo : singleMember.getRepositoriesContributedTo().getNodes()) {
            String committedRepoID = nodesRepoContributedTo.getId();
            for (NodesHistory nodesHistory : nodesRepoContributedTo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                if (committedRepos.containsKey(committedRepoID)) {
                    committedRepos.get(committedRepoID).add(nodesHistory.getCommittedDate());
                } else
                    committedRepos.put(committedRepoID, new ArrayList<>(Arrays.asList(nodesHistory.getCommittedDate())));

                commitsDates.add(nodesHistory.getCommittedDate());
            }
        }

        members.put(singleMember.getId(), new Member(
                singleMember.getName(),
                singleMember.getLogin(),
                singleMember.getAvatarUrl(),
                singleMember.getUrl(),
                commitsDates.size(),
                issuesDates.size(),
                pullRequestDates.size(),
                this.generateChartJSData(commitsDates),
                this.generateChartJSData(issuesDates),
                this.generateChartJSData(pullRequestDates)));

        return new ResponseWrapper(members, committedRepos);
    }
}
