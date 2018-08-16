package Tasks;

import config.RateLimitConfig;
import enums.RequestStatus;
import enums.RequestType;
import objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import processors.ResponseProcessor;
import processors.ResponseProcessorManager;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;

import java.util.*;

public class ResponseProcessorTask extends ResponseProcessor {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    /**
     * Scheduled task for checking all the saved queries response status. If there is a valid response the response processing begins.
     * The selected query is processed in the ProcessorManager which selects the suitable response processor. After successful processing the query is deleted.
     */
    @Scheduled(fixedRate = 500)
    private void processCrawledQueryData() {
        if (!requestRepository.findByQueryStatus(RequestStatus.VALID_ANSWER_RECEIVED).isEmpty()) {
            Query processingQuery = requestRepository.findByQueryStatus(RequestStatus.VALID_ANSWER_RECEIVED).get(0);
            ResponseWrapper response = new ResponseProcessorManager(processingQuery).processResponse();
            this.processResponse(processingQuery, response);
            requestRepository.delete(processingQuery);
        }
    }

    private void processResponse(Query processingQuery, ResponseWrapper responseWrapper) {
        OrganizationWrapper organization = organizationRepository.findByOrganizationName(processingQuery.getOrganizationName());
        switch (processingQuery.getQueryRequestType()) {
            case ORGANIZATION_VALIDATION:
                this.processOrganizationValidation(responseWrapper, processingQuery);
                break;
            case ORGANIZATION_DETAIL:
                this.processOrganizationDetailResponse(organization, responseWrapper, processingQuery);
                break;
            case MEMBER_ID:
                this.processMemberIDResponse(organization, responseWrapper, processingQuery);
                break;
            case MEMBER_PR:
                this.processMemberPRResponse(organization, responseWrapper, processingQuery);
                break;
            case MEMBER:
                this.processMemberResponse(organization, responseWrapper, processingQuery);
                break;
            case REPOSITORY:
                this.processRepositoryResponse(organization, responseWrapper, processingQuery);
                break;
            case TEAM:
                this.processOrganizationTeams(organization, responseWrapper, processingQuery);
                break;
            case EXTERNAL_REPO:
                this.processExternalRepo(organization, responseWrapper, processingQuery);
                break;
            case CREATED_REPOS_BY_MEMBERS:
                this.processCreatedReposByMembers(organization, responseWrapper, processingQuery);
                break;
        }
        processingQuery.setQueryStatus(RequestStatus.FINISHED);
    }

    private void processOrganizationValidation(ResponseWrapper responseWrapper, Query processingQuery) {
        if (responseWrapper.isValid()) {
            requestRepository.saveAll(new RequestManager(processingQuery.getOrganizationName()).generateAllRequests());
            OrganizationWrapper organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organizationRepository.save(organization);
        }
    }

    private void processCreatedReposByMembers(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        organization.addCreatedReposByMembers(responseWrapper.getCreatedRepositoriesByMembers().getRepositories());
        this.doFinishingQueryProcedure(organization, processingQuery, RequestType.CREATED_REPOS_BY_MEMBERS);
    }

    private void processExternalRepo(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        organization.addExternalRepos(responseWrapper.getRepositories().getRepositories());
        this.processExternalReposAndFindContributors(organization, processingQuery);
        this.doFinishingQueryProcedure(organization, processingQuery, RequestType.EXTERNAL_REPO);
    }

    private void processExternalReposAndFindContributors(OrganizationWrapper organization, Query processingQuery) {
        if (this.checkIfQueryIsLastOfRequestType(organization, processingQuery, RequestType.EXTERNAL_REPO)) {
            HashMap<String, ArrayList<String>> externalRepos = this.calculateExternalRepoContributions(organization);
            for (String externalRepoID : externalRepos.keySet()) {
                for (String contributorID : externalRepos.get(externalRepoID)) {
                    Repository suitableExternalRepo = organization.getExternalRepos().containsKey(externalRepoID) ? organization.getExternalRepos().get(externalRepoID) : null;
                    if (suitableExternalRepo != null) {
                        if (suitableExternalRepo.getContributor() != null) {
                            suitableExternalRepo.addContributor(organization.getMembers().containsKey(contributorID) ? organization.getMembers().get(contributorID) : null);
                        } else {
                            ArrayList<Member> contributors = new ArrayList<>();
                            contributors.add(organization.getMembers().containsKey(contributorID) ? organization.getMembers().get(contributorID) : null);
                            suitableExternalRepo.setContributor(contributors);
                        }
                    }
                }
            }
        }
    }

    private void processOrganizationTeams(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        organization.addTeams(responseWrapper.getTeams().getTeams());
        if (responseWrapper.getTeams().isHasNextPage()) {
            this.generateNextRequests(processingQuery.getOrganizationName(), responseWrapper.getRepositories().getEndCursor(), RequestType.TEAM);
        }
        this.doFinishingQueryProcedure(organization, processingQuery, RequestType.TEAM);
    }

    private void processRepositoryResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        organization.addRepositories(responseWrapper.getRepositories().getRepositories());
        if (responseWrapper.getRepositories().isHasNextPage()) {
            this.generateNextRequests(processingQuery.getOrganizationName(), responseWrapper.getRepositories().getEndCursor(), RequestType.REPOSITORY);
        } else {
            if (organization.getFinishedRequests().contains(RequestType.MEMBER_PR)) {
                organization.getOrganizationDetail().setNumOfExternalRepoContributions(calculateExternalRepoContributions(organization).size());
            }
        }
        this.doFinishingQueryProcedure(organization, processingQuery, RequestType.REPOSITORY);
    }

    private void processOrganizationDetailResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        organization.setOrganizationDetail(responseWrapper.getOrganizationDetail());
        organization.addMemberAmount(responseWrapper.getOrganizationDetail().getNumOfMembers());
        this.doFinishingQueryProcedure(organization, processingQuery, RequestType.ORGANIZATION_DETAIL);
    }

    private void processMemberIDResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        organization.addMemberIDs(responseWrapper.getMemberID().getMemberIDs());
        if (responseWrapper.getMemberID().isHasNextPage()) {
            this.generateNextRequests(processingQuery.getOrganizationName(), responseWrapper.getMemberID().getEndCursor(), RequestType.MEMBER_ID);
        } else {
            for (String memberID : organization.getMemberIDs()) {
                requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), memberID, RequestType.MEMBER).generateRequest(RequestType.MEMBER));
                requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), memberID, RequestType.CREATED_REPOS_BY_MEMBERS).generateRequest(RequestType.CREATED_REPOS_BY_MEMBERS));
            }
        }
        this.doFinishingQueryProcedure(organization, processingQuery, RequestType.MEMBER_ID);
    }

    private void processMemberPRResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        organization.addMemberPRs(responseWrapper.getMemberPR().getMemberPRrepositoryIDs());

        if (responseWrapper.getMemberPR().isHasNextPage()) {
            this.generateNextRequests(processingQuery.getOrganizationName(), responseWrapper.getMemberPR().getEndCursor(), RequestType.MEMBER_PR);
        } else {
            if (organization.getFinishedRequests().contains(RequestType.REPOSITORY)) {
                this.calculateExternalOrganizationPullRequestsChartJSData(organization, responseWrapper.getPullRequestsDates());
                organization.getOrganizationDetail().setNumOfExternalRepoContributions(this.calculateExternalRepoContributions(organization).size());
            }

            Set<String> repoIDs = this.calculateExternalRepoContributions(organization).keySet();
            while (!repoIDs.isEmpty()) {
                Set<String> subSet = new HashSet<>(new ArrayList<>(repoIDs).subList(0, Math.min(9, repoIDs.size())));
                List<String> targetList = new ArrayList<>(subSet);
                requestRepository.save(new RequestManager(targetList, processingQuery.getOrganizationName()).generateRequest(RequestType.EXTERNAL_REPO));
                repoIDs.removeAll(subSet);
            }
        }
        this.doFinishingQueryProcedure(organization, processingQuery, RequestType.MEMBER_PR);
    }

    private void processMemberResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        organization.addMembers(responseWrapper.getMembers());

        if (this.checkIfQueryIsLastOfRequestType(organization, processingQuery, RequestType.MEMBER)) {
            this.calculateInternalOrganizationCommitsChartJSData(organization, responseWrapper.getComittedRepos());
        }
        this.doFinishingQueryProcedure(organization, processingQuery, RequestType.MEMBER);
    }

    /**
     * Calculates the internal commits of the members in the own organization repositories. Generated as ChartJSData and saved in OrganizationDetail.
     *
     * @param organization
     */
    private void calculateInternalOrganizationCommitsChartJSData(OrganizationWrapper organization, HashMap<String, ArrayList<Calendar>> comittedRepo) {
        Set<String> organizationRepoIDs = organization.getRepositories().keySet();
        ArrayList<Calendar> internalCommitsDates = new ArrayList<>();
        for (String id : organizationRepoIDs) {
            if (comittedRepo.containsKey(id)) {
                internalCommitsDates.addAll(comittedRepo.get(id));
            }
        }

        organization.getOrganizationDetail().setInternalRepositoriesCommits(this.generateChartJSData(internalCommitsDates));

    }

    /**
     * Calculates the external pull requests of the members. Generated as ChartJSData and saved in OrganizationDetail.
     *
     * @param organization
     */
    private void calculateExternalOrganizationPullRequestsChartJSData(OrganizationWrapper organization, HashMap<String, ArrayList<Calendar>> contributedRepos) {
        Set<String> organizationRepoIDs = organization.getRepositories().keySet();
        ArrayList<Calendar> externalPullRequestsDates = new ArrayList<>();
        contributedRepos.keySet().removeAll(organizationRepoIDs);
        for (ArrayList<Calendar> calendar : contributedRepos.values()) {
            externalPullRequestsDates.addAll(calendar);
        }

        organization.getOrganizationDetail().setExternalRepositoriesPullRequests(this.generateChartJSData(externalPullRequestsDates));
    }

    private HashMap<String, ArrayList<String>> calculateExternalRepoContributions(OrganizationWrapper organization) {
        HashMap<String, ArrayList<String>> externalContributions = new HashMap<>();
        externalContributions.putAll(organization.getMemberPRRepoIDs());
        externalContributions.keySet().removeAll(organization.getRepositories().keySet());
        return externalContributions;
    }

    private void checkIfOrganizationUpdateIsFinished(OrganizationWrapper organization) {
        if (organization.getFinishedRequests().size() == RequestType.values().length - 1) {
            organization.setLastUpdateTimestamp(new Date());
            System.out.println("Complete Update Cost: " + organization.getCompleteUpdateCost());
        }
        organizationRepository.save(organization);
    }

    private boolean checkIfQueryIsLastOfRequestType(OrganizationWrapper organization, Query processingQuery, RequestType requestType) {
        if (requestRepository.findByQueryRequestTypeAndOrganizationName(requestType, processingQuery.getOrganizationName()).size() == 1) {
            organization.addFinishedRequest(requestType);
            return true;
        }
        return false;
    }


    private void doFinishingQueryProcedure(OrganizationWrapper organization, Query processingQuery, RequestType requestType) {
        organizationRepository.save(organization);
        organization.setCompleteUpdateCost(RateLimitConfig.getPreviousRequestCostAndRequestType().get(requestType));
        this.checkIfQueryIsLastOfRequestType(organization, processingQuery, requestType);
        this.checkIfOrganizationUpdateIsFinished(organization);
    }


    private void generateNextRequests(String organizationName, String endCursor, RequestType requestType) {
        requestRepository.save(new RequestManager(organizationName, endCursor).generateRequest(requestType));
    }

}
