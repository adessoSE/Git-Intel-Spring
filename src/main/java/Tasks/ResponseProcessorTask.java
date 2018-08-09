package Tasks;

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
                processCreatedReposByMembers(organization, responseWrapper, processingQuery);
                break;
        }
        processingQuery.setQueryStatus(RequestStatus.FINISHED);
    }

    private void processOrganizationValidation(ResponseWrapper responseWrapper, Query processingQuery) {
        if (responseWrapper.isValid()) {
            requestRepository.saveAll(new RequestManager(processingQuery.getOrganizationName()).generateAllRequests());
        }
    }

    private void processCreatedReposByMembers(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery){
        if (organization != null) {
            organization.addCreatedReposByMembers(responseWrapper.getCreatedRepositoriesByMembers().getRepositories());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setCreatedReposByMembers(responseWrapper.getCreatedRepositoriesByMembers().getRepositories());
        }
        if (requestRepository.findByQueryRequestTypeAndOrganizationName(RequestType.CREATED_REPOS_BY_MEMBERS, processingQuery.getOrganizationName()).size() == 1) {
            organization.addFinishedRequest(RequestType.CREATED_REPOS_BY_MEMBERS);
        }
        organizationRepository.save(organization);
        this.checkIfUpdateIsFinished(organization);
    }

    private void processExternalRepo(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.addExternalRepos(responseWrapper.getRepositories().getRepositories());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setExternalRepos(responseWrapper.getRepositories().getRepositories());
        }
        if(requestRepository.findByQueryRequestTypeAndOrganizationName(RequestType.EXTERNAL_REPO, organization.getOrganizationName()).size() == 1){
            HashMap<String,ArrayList<String>> externalRepos = this.calculateExternalRepoContributions(organization);
            for (String externalRepoID : externalRepos.keySet()){
                    for(String contributorID : externalRepos.get(externalRepoID)){
                        Repository suitableExternalRepo = organization.getExternalRepos().containsKey(externalRepoID) ? organization.getExternalRepos().get(externalRepoID) : null;
                        if(suitableExternalRepo != null){
                            if(suitableExternalRepo.getContributor() != null){
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
        if (requestRepository.findByQueryRequestTypeAndOrganizationName(RequestType.EXTERNAL_REPO, processingQuery.getOrganizationName()).size() == 1) {
            organization.addFinishedRequest(RequestType.EXTERNAL_REPO);
        }
        organizationRepository.save(organization);
        this.checkIfUpdateIsFinished(organization);
    }

    private void processOrganizationTeams(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery){
        if (organization != null) {
            organization.addTeams(responseWrapper.getTeams().getTeams());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setTeams(responseWrapper.getTeams().getTeams());
        }
        organizationRepository.save(organization);
        if (responseWrapper.getTeams().isHasNextPage()) {
            requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), responseWrapper.getRepositories().getEndCursor()).generateRequest(RequestType.TEAM));
        } else {
            organization.addFinishedRequest(RequestType.TEAM);
            organizationRepository.save(organization);
        }
        this.checkIfUpdateIsFinished(organization);
    }

    private void processRepositoryResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.addRepositories(responseWrapper.getRepositories().getRepositories());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setRepositories(responseWrapper.getRepositories().getRepositories());
        }
        organizationRepository.save(organization);
        if (responseWrapper.getRepositories().isHasNextPage()) {
            requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), responseWrapper.getRepositories().getEndCursor()).generateRequest(RequestType.REPOSITORY));
        } else {
            if(organization.getFinishedRequests().contains(RequestType.MEMBER_PR)){
                organization.getOrganizationDetail().setNumOfExternalRepoContributions(calculateExternalRepoContributions(organization).size());
            }
            organization.addFinishedRequest(RequestType.REPOSITORY);
            organizationRepository.save(organization);
        }
        this.checkIfUpdateIsFinished(organization);
    }

    private void processOrganizationDetailResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.setOrganizationDetail(responseWrapper.getOrganizationDetail());
            organization.addMemberAmount(responseWrapper.getOrganizationDetail().getNumOfMembers());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setOrganizationDetail(responseWrapper.getOrganizationDetail());
            organization.addMemberAmount(responseWrapper.getOrganizationDetail().getNumOfMembers());
        }
        organization.addFinishedRequest(RequestType.ORGANIZATION_DETAIL);
        organizationRepository.save(organization);
        this.checkIfUpdateIsFinished(organization);
    }

    private void processMemberIDResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.addMemberIDs(responseWrapper.getMemberID().getMemberIDs());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setMemberIDs(responseWrapper.getMemberID().getMemberIDs());
        }
        organizationRepository.save(organization);
        if (responseWrapper.getMemberID().isHasNextPage()) {
            requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), responseWrapper.getMemberID().getEndCursor()).generateRequest(RequestType.MEMBER_ID));
        } else {
            organization.addFinishedRequest(RequestType.MEMBER_ID);
            organizationRepository.save(organization);
            ArrayList<String> memberIDs = organizationRepository.findByOrganizationName(processingQuery.getOrganizationName()).getMemberIDs();
            while (!memberIDs.isEmpty()) {
                requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), memberIDs.get(0), RequestType.MEMBER).generateRequest(RequestType.MEMBER));
                requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), memberIDs.get(0), RequestType.CREATED_REPOS_BY_MEMBERS).generateRequest(RequestType.CREATED_REPOS_BY_MEMBERS));
                memberIDs.removeAll(Arrays.asList(memberIDs.get(0)));
            }
        }
        this.checkIfUpdateIsFinished(organization);
    }

    private void processMemberPRResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.addMemberPRs(responseWrapper.getMemberPR().getMemberPRrepositoryIDs());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setMemberPRRepoIDs(responseWrapper.getMemberPR().getMemberPRrepositoryIDs());
        }
        if (responseWrapper.getMemberPR().isHasNextPage()) {
            requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), responseWrapper.getMemberPR().getEndCursor()).generateRequest(RequestType.MEMBER_PR));
        } else {
            if(organization.getFinishedRequests().contains(RequestType.REPOSITORY)){
                calculateExternalOrganizationPullRequestsChartJSData(organization,responseWrapper.getPullRequestsDates());
                organization.getOrganizationDetail().setNumOfExternalRepoContributions(calculateExternalRepoContributions(organization).size());
            }

            Set<String> repoIDs = calculateExternalRepoContributions(organization).keySet();
            while (!repoIDs.isEmpty()) {
                Set<String> subSet = new HashSet<>(new ArrayList<>(repoIDs).subList(0, Math.min(9, repoIDs.size())));
                List<String> targetList = new ArrayList<>(subSet);
                requestRepository.save(new RequestManager(targetList, processingQuery.getOrganizationName()).generateRequest(RequestType.EXTERNAL_REPO));
                repoIDs.removeAll(subSet);
            }
            organization.addFinishedRequest(RequestType.MEMBER_PR);
        }
        organizationRepository.save(organization);
        this.checkIfUpdateIsFinished(organization);
    }
    private void processMemberResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.addMembers(responseWrapper.getMembers());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setMembers(responseWrapper.getMembers());
        }
        if (requestRepository.findByQueryRequestTypeAndOrganizationName(RequestType.MEMBER, processingQuery.getOrganizationName()).size() == 1) {
            this.calculateInternalOrganizationCommitsChartJSData(organization, responseWrapper.getComittedRepos());
            organization.addFinishedRequest(RequestType.MEMBER);
        }
        organizationRepository.save(organization);
        this.checkIfUpdateIsFinished(organization);
    }

    /**
     * Calculates the internal commits of the members in the own organization repositories. Generated as ChartJSData and saved in OrganizationDetail.
     * @param organization
     */
    private void calculateInternalOrganizationCommitsChartJSData(OrganizationWrapper organization, HashMap<String, ArrayList<Calendar>> comittedRepo) {
        Set<String> organizationRepoIDs = organization.getRepositories().keySet();
        ArrayList<Calendar> internalCommitsDates = new ArrayList<>();
        for(String id : organizationRepoIDs){
            if (comittedRepo.containsKey(id)){
                internalCommitsDates.addAll(comittedRepo.get(id));
            }
        }

        organization.getOrganizationDetail().setInternalRepositoriesCommits(this.generateChartJSData(internalCommitsDates));

    }

    /**
     * Calculates the external pull requests of the members. Generated as ChartJSData and saved in OrganizationDetail.
     * @param organization
     */
    private void calculateExternalOrganizationPullRequestsChartJSData(OrganizationWrapper organization, HashMap<String, ArrayList<Calendar>> contributedRepos) {
        Set<String> organizationRepoIDs = organization.getRepositories().keySet();
        ArrayList<Calendar> externalPullRequestsDates = new ArrayList<>();
        contributedRepos.keySet().removeAll(organizationRepoIDs);
        for(ArrayList<Calendar> calendar : contributedRepos.values()){
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

    private void checkIfUpdateIsFinished(OrganizationWrapper organization){
        if (organization.getFinishedRequests().size() == RequestType.values().length){
            organization.setLastUpdateTimestamp(new Date());
            organizationRepository.save(organization);
        }
    }

}
