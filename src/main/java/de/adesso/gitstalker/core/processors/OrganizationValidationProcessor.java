package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
import de.adesso.gitstalker.core.resources.organization_validation.Data;
import de.adesso.gitstalker.core.resources.organization_validation.ResponseOrganizationValidation;


public class OrganizationValidationProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

    public OrganizationValidationProcessor() {
    }

    private void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.organization = this.organizationRepository.findByOrganizationName(requestQuery.getOrganizationName());
    }

    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository);
        Data responseData = ((ResponseOrganizationValidation) this.requestQuery.getQueryResponse()).getData();
        if (this.processQueryResponse(responseData)) {
            this.organization = this.generateOrganizationWrapper(this.requestQuery.getOrganizationName());
            this.requestRepository.saveAll(new RequestManager(this.requestQuery.getOrganizationName()).generateAllRequests());
            super.updateRateLimit(responseData.getRateLimit(), requestQuery.getQueryRequestType());
            super.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.organization, this.requestQuery, RequestType.ORGANIZATION_VALIDATION);
        }
    }

    private OrganizationWrapper generateOrganizationWrapper(String organizationName) {
        if (this.organization != null) {
            return this.organization;
        } else return new OrganizationWrapper(organizationName);
    }

    private boolean processQueryResponse(Data responseData) {
        return responseData.getOrganization() != null;
    }
}
