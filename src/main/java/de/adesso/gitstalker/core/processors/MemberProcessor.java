package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Member;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.member_Resources.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class MemberProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

    private HashMap<String, ArrayList<Calendar>> committedRepos = new HashMap<>();
    private HashMap<String, Member> members = new HashMap<>();

    public MemberProcessor() {
    }

    private void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.organization = this.organizationRepository.findByOrganizationName(requestQuery.getOrganizationName());
    }

    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository);
        super.updateRateLimit(this.requestQuery.getQueryResponse().getResponseMember().getData().getRateLimit(), requestQuery.getQueryRequestType());
        this.processQueryResponse(this.requestQuery.getQueryResponse().getResponseMember().getData().getNode());
        this.calculatesInternalOrganizationCommits();
        this.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.organization, this.requestQuery, RequestType.MEMBER);
    }

    protected void calculatesInternalOrganizationCommits() {
        if (this.checkIfQueryIsLastOfRequestType(this.organization, this.requestQuery, RequestType.MEMBER, requestRepository)) {
            organization.addMembers(this.members);
            super.calculateInternalOrganizationCommitsChartJSData(organization, this.committedRepos);
        }
    }

    protected Calendar configureCalendarToFitCrawlingPeriod() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - Config.PAST_DAYS_AMOUNT_TO_CRAWL);
        return calendar;
    }

    protected void processQueryResponse(User singleMember) {

        HashMap<String, String> previousCommitsWithLink = new HashMap<>();
        HashMap<String, String> previousIssuesWithLink = new HashMap<>();
        HashMap<String, String> previousPullRequestsWithLink = new HashMap<>();

        ArrayList<Calendar> pullRequestDates = new ArrayList<>();
        ArrayList<Calendar> issuesDates = new ArrayList<>();
        ArrayList<Calendar> commitsDates = new ArrayList<>();

        Calendar cal = this.configureCalendarToFitCrawlingPeriod();

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
                if (this.committedRepos.containsKey(committedRepoID)) {
                    this.committedRepos.get(committedRepoID).add(nodesHistory.getCommittedDate());
                } else
                    this.committedRepos.put(committedRepoID, new ArrayList<>(Arrays.asList(nodesHistory.getCommittedDate())));
                previousCommitsWithLink.put(nodesHistory.getCommittedDate().getTime().toString(), nodesHistory.getUrl());
                commitsDates.add(nodesHistory.getCommittedDate());
            }
        }

        this.members.put(singleMember.getId(), new Member(
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
    }
}
