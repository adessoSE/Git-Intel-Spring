package Tasks;

import enums.RequestStatus;
import enums.RequestType;
import objects.*;
import objects.Member.Member;
import objects.Organization.OrganizationWrapper;
import objects.Repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import processors.ResponseProcessorManager;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;

import java.util.*;

public class ResponseProcessorTask {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    /**
     * Scheduled task for checking all the saved queries response status. If there is a valid response the response processing begins.
     * The selected query is processed in the ProcessorManager which selects the suitable response processor. After successful processing the query is deleted.
     */
    @Scheduled(fixedRate = 2000)
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
        }
        processingQuery.setQueryStatus(RequestStatus.FINISHED);
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
            organization.addFinishedRequest(RequestType.EXTERNAL_REPO);
        organizationRepository.save(organization);
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
                requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), memberIDs.subList(0, Math.min(9, memberIDs.size()))).generateRequest(RequestType.MEMBER));
                memberIDs.removeAll(memberIDs.subList(0, Math.min(9, memberIDs.size())));
            }
        }
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
    }
    private void processMemberResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.addMembers(responseWrapper.getMembers());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setMembers(responseWrapper.getMembers());
        }
        if (requestRepository.findByQueryRequestTypeAndOrganizationName(RequestType.MEMBER, processingQuery.getOrganizationName()).size() == 1) {
            this.calculateOrganizationChartJSData(organization);
            organization.addFinishedRequest(RequestType.MEMBER);
        }
        organizationRepository.save(organization);
    }

    /**
     * Add up every member's ChartJSData for commits, issues and pull requests to save numbers for the whole organization
     * @param organization
     */
    private void calculateOrganizationChartJSData(OrganizationWrapper organization) {
        // Instantiate ArrayLists to save final values in and initialize them "empty" to be able to access and add up values
        String firstMember = organization.getMemberIDs().get(0);
        ArrayList<String> chartJSLabels = organization.getMembers().get(firstMember).getPreviousCommits().getChartJSLabels();
        ArrayList<Integer> chartJSCommitData = new ArrayList<>();
        ArrayList<Integer> chartJSIssueData = new ArrayList<>();
        ArrayList<Integer> chartJSPRData = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            chartJSCommitData.add(0);
            chartJSIssueData.add(0);
            chartJSPRData.add(0);
        }
        // Walk through members and add up values for commits, issues and pull requests
        for (Member member : organization.getMembers().values()) {
            for (int i = 0; i <= member.getPreviousCommits().getChartJSDataset().size() - 1; i++) {
                chartJSCommitData.set(i, chartJSCommitData.get(i) + member.getPreviousCommits().getChartJSDataset().get(i));
                chartJSIssueData.set(i, chartJSIssueData.get(i) + member.getPreviousIssues().getChartJSDataset().get(i));
                chartJSPRData.set(i, chartJSPRData.get(i) + member.getPreviousPullRequests().getChartJSDataset().get(i));
            }
        }
        // Add commits, issues and pull requests to OrganizationDetail object
        organization.getOrganizationDetail().setPreviousCommits(new ChartJSData(chartJSLabels, chartJSCommitData));
        organization.getOrganizationDetail().setPreviousIssues(new ChartJSData(chartJSLabels, chartJSIssueData));
        organization.getOrganizationDetail().setPreviousPullRequests(new ChartJSData(chartJSLabels, chartJSPRData));
    }

    private HashMap<String, ArrayList<String>> calculateExternalRepoContributions(OrganizationWrapper organization) {
        HashMap<String, ArrayList<String>> externalContributions = new HashMap<>();
        externalContributions.putAll(organization.getMemberPRRepoIDs());
        externalContributions.keySet().removeAll(organization.getRepositories().keySet());
        return externalContributions;
    }
}
