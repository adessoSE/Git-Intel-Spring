package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.objects.Repository;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.repository_Resources.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

@NoArgsConstructor
public class RepositoryProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private ProcessingRepository processingRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

    private HashMap<String, Repository> repositories = new HashMap<>();

    /**
     * Setting up the necessary parameters for the response processing.
     * @param requestQuery Query to be processed.
     * @param requestRepository RequestRepository for accessing requests.
     * @param organizationRepository OrganizationRepository for accessing organization.
     */
    private void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository, ProcessingRepository processingRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.processingRepository = processingRepository;
        this.organization = this.organizationRepository.findByOrganizationName(requestQuery.getOrganizationName());
    }

    /**
     * Performs the complete processing of an answer.
     * @param requestQuery Query to be processed.
     * @param requestRepository RequestRepository for accessing requests.
     * @param organizationRepository OrganizationRepository for accessing organization.
     */
    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository, ProcessingRepository processingRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository, processingRepository);
        Data responseData = ((ResponseRepository) this.requestQuery.getQueryResponse()).getData();
        super.updateRateLimit(responseData.getRateLimit(), requestQuery.getQueryRequestType());
        this.processQueryResponse(responseData.getOrganization().getRepositories());
        this.processRequestForRemainingInformation(responseData.getOrganization().getRepositories().getPageInfo(), requestQuery.getOrganizationName());
        super.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.processingRepository, this.organization, requestQuery, RequestType.REPOSITORY);
    }

    /**
     * Creates the subsequent requests if it becomes clear during processing that information is still open in the section.
     * If finished the repositories are added to the organization.
     * @param pageInfo Contains information required to define whether requests are still outstanding.
     * @param organizationName Organization name for creating the appropriate request
     */
    private void processRequestForRemainingInformation(PageInfo pageInfo, String organizationName) {
        if (pageInfo.isHasNextPage()) {
            super.generateNextRequests(organizationName, pageInfo.getEndCursor(), RequestType.REPOSITORY, requestRepository);
        } else {
            this.organization.addRepositories(this.repositories);
        }
    }

    /**
     * Processes the response of the requests.
     * @param repositoriesData Response information of the request
     */
    private void processQueryResponse(Repositories repositoriesData) {
        for (NodesRepositories repo : repositoriesData.getNodes()) {
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
            repositories.put(repo.getId(), new Repository()
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

    private String getLicense(NodesRepositories repo) {
        if (Objects.isNull(repo.getLicenseInfo())) return "No License deposited";
        else return repo.getLicenseInfo().getName();
    }

    private String getProgrammingLanguage(NodesRepositories repo) {
        if (Objects.isNull(repo.getPrimaryLanguage())) return "/";
        else return repo.getPrimaryLanguage().getName();
    }

    private String getDescription(NodesRepositories repo) {
        if (Objects.isNull(repo.getDescription())) return "No Description deposited";
        else return repo.getDescription();
    }
}
