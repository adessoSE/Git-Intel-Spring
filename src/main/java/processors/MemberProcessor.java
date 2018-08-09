package processors;

import config.Config;
import objects.Member;
import objects.Query;
import objects.ResponseWrapper;
import resources.member_Resources.*;

import java.util.*;

public class MemberProcessor extends ResponseProcessor {

    private Query requestQuery;

    public MemberProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        super.updateRateLimit(this.requestQuery.getQueryResponse().getResponseMember().getData().getRateLimit(), requestQuery.getQueryRequestType());

        HashMap<String, Member> members = new HashMap<>();
        User singleMember = this.requestQuery.getQueryResponse().getResponseMember().getData().getNode();

        HashMap<String, String> previousCommitsWithLink = new HashMap<>();
        HashMap<String, String> previousIssuesWithLink = new HashMap<>();
        HashMap<String, String> previousPullRequestsWithLink = new HashMap<>();

        ArrayList<Calendar> pullRequestDates = new ArrayList<>();
        ArrayList<Calendar> issuesDates = new ArrayList<>();
        ArrayList<Calendar> commitsDates = new ArrayList<>();

        HashMap<String, ArrayList<Calendar>> committedRepos = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.get(Calendar.DATE)-Config.PAST_DAYS_AMOUNT_TO_CRAWL);

            for (NodesPullRequests nodesPullRequests : singleMember.getPullRequests().getNodes()) {
                if (cal.before(nodesPullRequests.getCreatedAt())) {
                    pullRequestDates.add(nodesPullRequests.getCreatedAt());
                    previousPullRequestsWithLink.put(nodesPullRequests.getCreatedAt().getTime().toString(), nodesPullRequests.getUrl());
                }
            }
            for (NodesIssues nodesIssues : singleMember.getIssues().getNodes()) {
                if (cal.before(nodesIssues.getCreatedAt())) {
                    issuesDates.add(nodesIssues.getCreatedAt());
                  previousIssuesWithLink.put(nodesIssues.getCreatedAt().getTime().toString(), nodesIssues.getUrl());
                }
            }

        for (NodesRepoContributedTo nodesRepoContributedTo : singleMember.getRepositoriesContributedTo().getNodes()) {
            String committedRepoID = nodesRepoContributedTo.getId();
            for (NodesHistory nodesHistory : nodesRepoContributedTo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                if (committedRepos.containsKey(committedRepoID)) {
                    committedRepos.get(committedRepoID).add(nodesHistory.getCommittedDate());
                } else committedRepos.put(committedRepoID, new ArrayList<>(Arrays.asList(nodesHistory.getCommittedDate())));
                previousCommitsWithLink.put(nodesHistory.getCommittedDate().getTime().toString(), nodesHistory.getUrl());
                commitsDates.add(nodesHistory.getCommittedDate());
            }
        }

        members.put(singleMember.getId(), new Member(
                singleMember.getName(),
                singleMember.getLogin(),
                singleMember.getAvatarUrl(),
                singleMember.getUrl(),
                previousCommitsWithLink,
                previousIssuesWithLink,
                previousPullRequestsWithLink,
                this.generateChartJSData(commitsDates),
                this.generateChartJSData(issuesDates),
                this.generateChartJSData(pullRequestDates)));

        return new ResponseWrapper(members, committedRepos);
    }
}
