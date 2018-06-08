package Tasks;

import enums.RequestStatus;
import enums.RequestType;
import objects.OrganizationWrapper;
import objects.Query;
import objects.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import processors.ResponseProcessorManager;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;

import java.util.ArrayList;

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
            case REPOSITORY_ID:
                this.processRepositoryIDResponse(organization, responseWrapper, processingQuery);
                break;
            case MEMBER:
                this.processMemberResponse(organization, responseWrapper, processingQuery);
                break;
            case REPOSITORY:
                this.processRepositoryResponse(organization, responseWrapper, processingQuery);
                break;
        }
        processingQuery.setQueryStatus(RequestStatus.FINISHED);
    }

    private void processRepositoryResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery){
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
            organization.addFinishedRequest(RequestType.REPOSITORY);
            organizationRepository.save(organization);
        }
    }

    private void processOrganizationDetailResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.setOrganizationDetail(responseWrapper.getOrganizationDetail());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setOrganizationDetail(responseWrapper.getOrganizationDetail());
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
        } else organization.addFinishedRequest(RequestType.MEMBER_PR);
        organizationRepository.save(organization);
    }

    private void processRepositoryIDResponse(OrganizationWrapper organization, ResponseWrapper responseWrapper, Query processingQuery) {
        if (organization != null) {
            organization.addOrganizationRepoIDs(responseWrapper.getRepositoryID().getRepositoryIDs());
        } else {
            organization = new OrganizationWrapper(processingQuery.getOrganizationName());
            organization.setOrganizationRepoIDs(responseWrapper.getRepositoryID().getRepositoryIDs());
        }
        if (responseWrapper.getRepositoryID().isHasNextPage()) {
            requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), responseWrapper.getRepositoryID().getEndCursor()).generateRequest(RequestType.REPOSITORY_ID));
        } else organization.addFinishedRequest(RequestType.REPOSITORY_ID);
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
            organization.addFinishedRequest(RequestType.MEMBER);
        }
        organizationRepository.save(organization);
    }
}
