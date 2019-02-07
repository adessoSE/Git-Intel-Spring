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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrganizationValidationProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private ProcessingRepository processingRepository;

    private Query requestQuery;
    private OrganizationWrapper organization;

    @Autowired
    public OrganizationValidationProcessor(RequestRepository requestRepository, OrganizationRepository organizationRepository, ProcessingRepository processingRepository) {
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
        Data responseData = ((ResponseOrganizationValidation) this.requestQuery.getQueryResponse()).getData();
        if (Objects.nonNull(responseData)) {
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
        if (Objects.nonNull(this.organization)) {
            return this.organization;
        } else return new OrganizationWrapper(organizationName);
    }
}
