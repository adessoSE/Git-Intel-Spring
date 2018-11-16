package de.adesso.gitstalker.core.Tasks;

import de.adesso.gitstalker.core.REST.OrganizationController;
import de.adesso.gitstalker.core.REST.responses.ProcessingOrganization;
import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.enums.RequestStatus;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import de.adesso.gitstalker.core.processors.ResponseProcessor;
import de.adesso.gitstalker.core.processors.ResponseProcessorManager;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import java.util.ArrayList;
import java.util.Map;

public class ResponseProcessorTask extends ResponseProcessor {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    private ResponseProcessorManager responseProcessorManager = new ResponseProcessorManager();

    /**
     * Automatic process for processing queries with valid responses. The query is passed in to the ResponseProcessorManager, which finds the appropriate processor.
     * After processing, the query is deleted.
     */
    @Scheduled(fixedRate = Config.PROCESSING_RATE_IN_MS)
    private void processCrawledQueryData() {
        ArrayList<Query> queriesToProcess;
        if(!OrganizationController.processingOrganizations.isEmpty()){
            Map.Entry<String, ProcessingOrganization> processingOrganization = OrganizationController.processingOrganizations.entrySet().iterator().next();
            queriesToProcess = requestRepository.findByQueryStatusAndOrganizationName(RequestStatus.VALID_ANSWER_RECEIVED, processingOrganization.getKey());
        } else queriesToProcess = requestRepository.findByQueryStatus(RequestStatus.VALID_ANSWER_RECEIVED);

        for (Query processableQuery : queriesToProcess) {
            if (this.checkIfNecessaryDataIsAvailable(processableQuery.getQueryRequestType(), processableQuery.getOrganizationName())) {
                responseProcessorManager.processResponse(this.organizationRepository, this.requestRepository, processableQuery);
                requestRepository.delete(processableQuery);
            } else continue;
        }
    }

    /**
     * Validation if the necessary data has already been collected to process the selected query
     *
     * @param requestType      Requires requestType to find pending requests of type
     * @param organizationName Requires organizationName to find pending requests for the associated organization
     * @return Returns if the query can be processed or not
     */
    private boolean checkIfNecessaryDataIsAvailable(RequestType requestType, String organizationName) {
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(organizationName);
        switch (requestType) {
            case TEAM:
                return organization.getFinishedRequests().contains(RequestType.MEMBER) && organization.getFinishedRequests().contains(RequestType.REPOSITORY);
            case MEMBER_PR:
                return organization.getFinishedRequests().contains(RequestType.REPOSITORY);
            case CREATED_REPOS_BY_MEMBERS:
                return organization.getFinishedRequests().contains(RequestType.MEMBER);
            default:
                return true;
        }
    }
}
