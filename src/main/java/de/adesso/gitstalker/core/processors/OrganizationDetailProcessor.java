package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationDetail;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.organisation_Resources.Data;
import de.adesso.gitstalker.core.resources.organisation_Resources.Organization;
import de.adesso.gitstalker.core.resources.organisation_Resources.ResponseOrganization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class OrganizationDetailProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private ProcessingRepository processingRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

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
        Data responseData = ((ResponseOrganization) this.requestQuery.getQueryResponse()).getData();
        super.updateRateLimit(responseData.getRateLimit(), requestQuery.getQueryRequestType());
        this.processQueryResponse(responseData);
        super.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.processingRepository, this.organization, requestQuery, RequestType.ORGANIZATION_DETAIL);
    }

    /**
     * Processes the response of the requests.
     * @param responseData Response information of the request
     */
    private void processQueryResponse(Data responseData) {
        Organization organization = responseData.getOrganization();

        this.organization.setOrganizationDetail(new OrganizationDetail(
                organization.getName(),
                organization.getDescription(),
                organization.getWebsiteUrl(),
                organization.getUrl(),
                organization.getLocation(),
                organization.getAvatarUrl(),
                organization.getMembers().getTotalCount(),
                organization.getRepositories().getTotalCount(),
                organization.getTeams().getTotalCount()));
        this.organization.addMemberAmount(organization.getMembers().getTotalCount());
    }
}
