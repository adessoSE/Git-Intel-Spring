package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
import de.adesso.gitstalker.core.resources.organization_validation.Data;
import de.adesso.gitstalker.core.resources.organization_validation.ResponseOrganizationValidation;
import lombok.NoArgsConstructor;


public class OrganizationValidationProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private ProcessingRepository processingRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

    public OrganizationValidationProcessor(String organizationName, RequestRepository requestRepository, OrganizationRepository organizationRepository, ProcessingRepository processingRepository) {
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.processingRepository = processingRepository;
        this.organization = this.organizationRepository.findByOrganizationName(organizationName);
    }

    /**
     * Performs the complete processing of an answer.
     * @param requestQuery Query to be processed.
     */
    public void processResponse(Query requestQuery) {
        this.requestQuery = requestQuery;
        Data responseData = ((ResponseOrganizationValidation) this.requestQuery.getQueryResponse()).getData();
        if (this.processQueryResponse(responseData)) {
            this.organization = this.generateOrganizationWrapper(this.requestQuery.getOrganizationName());
            this.requestRepository.saveAll(new RequestManager()
                    .setOrganizationName(this.requestQuery.getOrganizationName())
                    .generateAllRequests());
            super.updateRateLimit(responseData.getRateLimit(), requestQuery.getQueryRequestType());
            super.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.processingRepository, this.organization, this.requestQuery, RequestType.ORGANIZATION_VALIDATION);
        }
    }

    /**
     * Generates an organization wrapper object if the organization isn't assigned already.
     * @param organizationName Name assigned to the wrapper object
     * @return The assigned OrganizationWrapper or a new generated one.
     */
    private OrganizationWrapper generateOrganizationWrapper(String organizationName) {
        if (this.organization != null) {
            return this.organization;
        } else return new OrganizationWrapper(organizationName);
    }

    /**
     * Processes the response of the requests.
     * @param responseData Response information of the request
     */
    private boolean processQueryResponse(Data responseData) {
        return responseData.getOrganization() != null;
    }
}
