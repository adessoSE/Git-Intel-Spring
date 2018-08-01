package processors;

import objects.ChartJSData;
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
        HashMap<String, Member> members = new HashMap<>();
        User singleMember = this.requestQuery.getQueryResponse().getResponseMember().getData().getNode();

        ArrayList<Date> previousCommits = new ArrayList<>();
        ArrayList<Date> previousIssues = new ArrayList<>();
        ArrayList<Date> previousPullRequests = new ArrayList<>();

        HashMap<String, String> previousCommitsWithLink = new HashMap<>();
        HashMap<String, String> previousIssuesWithLink = new HashMap<>();
        HashMap<String, String> previousPullRequestsWithLink = new HashMap<>();

        HashMap<String, ArrayList<Date>> committedRepos = new HashMap<>();

        for (NodesPullRequests nodesPullRequests : singleMember.getPullRequests().getNodes()) {
            if (new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24)).getTime() < nodesPullRequests.getCreatedAt().getTime()) {
                previousPullRequests.add(nodesPullRequests.getCreatedAt());
                previousPullRequestsWithLink.put(nodesPullRequests.getCreatedAt().toString(), nodesPullRequests.getUrl());
            }
        }
        for (NodesIssues nodesIssues : singleMember.getIssues().getNodes()) {
            if (new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24)).getTime() < nodesIssues.getCreatedAt().getTime()) {
                previousCommits.add(nodesIssues.getCreatedAt());
                previousIssuesWithLink.put(nodesIssues.getCreatedAt().toString(), nodesIssues.getUrl());
            }
        }
        for (NodesRepoContributedTo nodesRepoContributedTo : singleMember.getRepositoriesContributedTo().getNodes()) {
            String committedRepoID = nodesRepoContributedTo.getId();
            for (NodesHistory nodesHistory : nodesRepoContributedTo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                if (committedRepos.containsKey(committedRepoID)) {
                    committedRepos.get(committedRepoID).add(nodesHistory.getCommittedDate());
                } else
                    committedRepos.put(committedRepoID, new ArrayList<>(Arrays.asList(nodesHistory.getCommittedDate())));
                previousCommits.add(nodesHistory.getCommittedDate());
                previousCommitsWithLink.put(nodesHistory.getCommittedDate().toString(), nodesHistory.getUrl());
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
                this.generateChartJSData(previousCommits),
                this.generateChartJSData(previousIssues),
                this.generateChartJSData(previousPullRequests)));

        return new ResponseWrapper(members, committedRepos);
    }
}
