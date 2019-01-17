package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Member;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.objects.Repository;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.externalRepo_Resources.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is the response processor used for ExternalRepo Request.
 */
@Getter
@Setter
public class ExternalRepoProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private ProcessingRepository processingRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

    private HashMap<String, Repository> repositoriesMap = new HashMap<>();

    public ExternalRepoProcessor(RequestRepository requestRepository, OrganizationRepository organizationRepository, ProcessingRepository processingRepository) {
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.processingRepository = processingRepository;
    }

    /**
     * Performs the complete processing of an answer.
     * @param requestQuery Query to be processed.
     */
    public void processResponse(Query requestQuery) {
        this.requestQuery = requestQuery;
        this.organization = this.organizationRepository.findByOrganizationName(requestQuery.getOrganizationName());
        Data repositoriesData = ((ResponseExternalRepository) this.requestQuery.getQueryResponse()).getData();

        super.updateRateLimit(repositoriesData.getRateLimit(), requestQuery.getQueryRequestType());
        this.processQueryResponse(repositoriesData.getNodes());
        this.processExternalReposAndFindContributors(organization, requestQuery);
        super.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.processingRepository, organization, requestQuery, RequestType.EXTERNAL_REPO);
    }

    /**
     * When all requests have been processed, a link is created to other collected information. This is done by linking the external repositories to the contributors.
     * @param organization Complete organization object for other data.
     * @param requestQuery Processed request query.
     */
    protected void processExternalReposAndFindContributors(OrganizationWrapper organization, Query requestQuery) {
        if (super.checkIfQueryIsLastOfRequestType(organization, requestQuery, RequestType.EXTERNAL_REPO, this.requestRepository)) {
            this.organization.addExternalRepos(this.repositoriesMap);
            HashMap<String, ArrayList<String>> externalRepos = super.calculateExternalRepoContributions(organization);
            for (String externalRepoID : externalRepos.keySet()) {
                for (String contributorID : externalRepos.get(externalRepoID)) {
                    Repository suitableExternalRepo = organization.getExternalRepos().getOrDefault(externalRepoID, null);
                    if (Objects.nonNull(suitableExternalRepo)) {
                        if (Objects.nonNull(suitableExternalRepo.getContributors())) {
                            suitableExternalRepo.addContributor(organization.getMembers().getOrDefault(contributorID, null));
                        } else {
                            ArrayList<Member> contributors = new ArrayList<>();
                            contributors.add(organization.getMembers().getOrDefault(contributorID, null));
                            suitableExternalRepo.setContributors(contributors);
                        }
                    }
                }
            }
        }
    }

    /**
     * Processes the individual repositories that were returned as replies.
     * @param repositories Repositories to be processed
     */
    protected void processQueryResponse(ArrayList<NodesRepositories> repositories) {
        for (NodesRepositories repo : repositories) {
            ArrayList<Calendar> pullRequestDates = new ArrayList<>();
            ArrayList<Calendar> issuesDates = new ArrayList<>();
            ArrayList<Calendar> commitsDates = new ArrayList<>();
          
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - Config.PAST_DAYS_AMOUNT_TO_CRAWL);

            for (NodesPullRequests nodesPullRequests : repo.getPullRequests().getNodes()) {
                if (cal.before(nodesPullRequests.getCreatedAt())) {
                    pullRequestDates.add(nodesPullRequests.getCreatedAt());
                }
            }
            for (NodesIssues nodesIssues : repo.getIssues().getNodes()) {
                if (cal.before(nodesIssues.getCreatedAt())) {
                    issuesDates.add(nodesIssues.getCreatedAt());
                }
            }
            if (Objects.nonNull(repo.getDefaultBranchRef())) {
                for (NodesHistory nodesHistory : repo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                    commitsDates.add(nodesHistory.getCommittedDate());
                }
            }
            this.repositoriesMap.put(repo.getId(), new Repository()
                    .setName(repo.getName())
                    .setUrl(repo.getUrl())
                    .setDescription(getDescription(repo))
                    .setProgrammingLanguage(getProgrammingLanguage(repo))
                    .setLicense(getLicense(repo))
                    .setForks(repo.getForkCount())
                    .setStars(repo.getStargazers().getTotalCount())
                    .setAmountPreviousCommits(commitsDates.size())
                    .setPreviousCommits(this.generateChartJSData(commitsDates))
                    .setAmountPreviousIssues(issuesDates.size())
                    .setPreviousIssues(this.generateChartJSData(issuesDates))
                    .setAmountPreviousPullRequests(pullRequestDates.size())
                    .setPreviousPullRequests(this.generateChartJSData(pullRequestDates)));
        }
    }

    protected String getLicense(NodesRepositories repo) {
        if (Objects.isNull(repo.getLicenseInfo())) return "No License deposited";
        else return repo.getLicenseInfo().getName();
    }

    protected String getProgrammingLanguage(NodesRepositories repo) {
        if (Objects.isNull(repo.getPrimaryLanguage())) return "/";
        else return repo.getPrimaryLanguage().getName();
    }

    protected String getDescription(NodesRepositories repo) {
        if (Objects.isNull(repo.getDescription())) return "No Description deposited";
        else return repo.getDescription();
    }

}
