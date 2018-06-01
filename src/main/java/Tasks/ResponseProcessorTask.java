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
                if (organization != null) {
                    organization.setOrganizationDetail(responseWrapper.getOrganizationDetail());
                } else {
                    organization = new OrganizationWrapper(processingQuery.getOrganizationName());
                    organization.setOrganizationDetail(responseWrapper.getOrganizationDetail());
                }
                break;
            case MEMBER_ID:
                if (organization != null) {
                    organization.addMemberIDs(responseWrapper.getMemberID().getMemberIDs());
                } else {
                    organization = new OrganizationWrapper(processingQuery.getOrganizationName());
                    organization.setMemberIDs(responseWrapper.getMemberID().getMemberIDs());
                }
                if (responseWrapper.getMemberID().isHasNextPage()) {
                    requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), responseWrapper.getMemberID().getEndCursor()).generateRequest(RequestType.MEMBER_ID));
                }
                break;
            case MEMBER_PR:
                if (organization != null) {
                    organization.addMemberPRs(responseWrapper.getMemberPR().getMemberPRrepositoryIDs());
                } else {
                    organization = new OrganizationWrapper(processingQuery.getOrganizationName());
                    organization.setMemberPRRepoIDs(responseWrapper.getMemberPR().getMemberPRrepositoryIDs());
                }
                if (responseWrapper.getMemberPR().isHasNextPage()) {
                    requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), responseWrapper.getMemberPR().getEndCursor()).generateRequest(RequestType.MEMBER_PR));
                }
                break;
            case REPOSITORY_ID:
                if (organization != null) {
                    organization.addOrganizationRepoIDs(responseWrapper.getRepositoryID().getRepositoryIDs());
                } else {
                    organization = new OrganizationWrapper(processingQuery.getOrganizationName());
                    organization.setOrganizationRepoIDs(responseWrapper.getRepositoryID().getRepositoryIDs());
                }
                if (responseWrapper.getRepositoryID().isHasNextPage()) {
                    requestRepository.save(new RequestManager(processingQuery.getOrganizationName(), responseWrapper.getRepositoryID().getEndCursor()).generateRequest(RequestType.REPOSITORY_ID));
                }
                break;
        }
        organizationRepository.save(organization);
        processingQuery.setQueryStatus(RequestStatus.FINISHED);
    }
}
