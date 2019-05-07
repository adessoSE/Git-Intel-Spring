package de.adesso.gitstalker.core.processors;


import de.adesso.gitstalker.core.REST.responses.ProcessingOrganization;
import de.adesso.gitstalker.core.Tasks.RequestProcessorTask;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
import de.adesso.gitstalker.core.resources.organization_validation.Organization;
import de.adesso.gitstalker.core.resources.organization_validation.ResponseOrganizationValidation;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class ProcessingInformationProcessor {

    private ProcessingRepository processingRepository;
    private OrganizationRepository organizationRepository;
    private RequestRepository requestRepository;

    private String organizationName;
    private Query validationQuery;
    private ResponseOrganizationValidation responseOrganizationValidation;

    public ProcessingInformationProcessor(String organizationName, ProcessingRepository processingRepository, OrganizationRepository organizationRepository, RequestRepository requestRepository) {
        this.organizationName = organizationName;
        this.processingRepository = processingRepository;
        this.organizationRepository = organizationRepository;
        this.requestRepository = requestRepository;
    }

    public void updateProcessingOrganizationInformation() {
        OrganizationWrapper organizationWrapper = this.organizationRepository.findByOrganizationName(this.organizationName);
        ProcessingOrganization processingOrganization = this.processingRepository.findByInternalOrganizationName(this.organizationName);
        processingOrganization.setCurrentPositionInQueue(this.calculatePositionInLinkedHashMap());
        if (Objects.nonNull(organizationWrapper)) {
            processingOrganization.setFinishedRequestTypes(organizationWrapper.getFinishedRequests());
            processingOrganization.getMissingRequestTypes().removeAll(organizationWrapper.getFinishedRequests());
        }
        this.processingRepository.save(processingOrganization);
    }

    public int calculatePositionInLinkedHashMap() {
        int calculatedPosition = 0;
        for (ProcessingOrganization processingOrganization : this.processingRepository.findAll()) {
            calculatedPosition += 1;
            if (processingOrganization.getInternalOrganizationName().matches(this.organizationName)) {
                break;
            }
        }
        return calculatedPosition;
    }

    public Query getOrganizationValidationResponse() {
        this.validationQuery = new RequestManager()
                .setOrganizationName(this.organizationName)
                .generateRequest(RequestType.ORGANIZATION_VALIDATION);
        this.validationQuery.crawlQueryResponse();
        this.responseOrganizationValidation = (ResponseOrganizationValidation) this.validationQuery.getQueryResponse();
        return this.validationQuery;
    }

    //TODO: Nullpointer exception if the token was not set or is invalid.
    public boolean checkIfOrganizationIsValid() {
        if (Objects.nonNull(this.responseOrganizationValidation)){
            return Objects.nonNull(this.responseOrganizationValidation.getData().getOrganization());
        } else {
            return !Objects.isNull(this.responseOrganizationValidation);
        }
    }

    public void addProcessingOrganizationInformationIfMissingForTheOrganization() {
        if (Objects.isNull(this.processingRepository.findByInternalOrganizationName(this.organizationName))) {
            this.processingRepository.save(this.generateProcessingOrganizationInformation(this.responseOrganizationValidation));
        }
        this.requestRepository.save(this.validationQuery);
    }

    private ProcessingOrganization generateProcessingOrganizationInformation(ResponseOrganizationValidation responseOrganizationValidation) {
        Organization organization = responseOrganizationValidation.getData().getOrganization();

        return new ProcessingOrganization()
                .setInternalOrganizationName(organizationName)
                .setProcessingMessage("Currently the organization is still under processing.")
                .setSearchedOrganization(organization.getName())
                .setMissingRequestTypes(new HashSet<>(Arrays.asList(RequestType.values())))
                .setFinishedRequestTypes(new HashSet<>())
                .setTotalCountOfNeededRequests(this.calculateTotalCountOfNeededRequests(organization))
                .setTotalCountOfRequestTypes(RequestType.values().length)
                .setCurrentPositionInQueue(this.calculatePositionInLinkedHashMap() + 1);
    }

    private int calculateTotalCountOfNeededRequests(Organization organization) {
        int memberTotalCount = organization.getMembersWithRole().getTotalCount();
        int teamTotalCount = organization.getTeams().getTotalCount();
        int repositoriesTotalCount = organization.getRepositories().getTotalCount();

        return (memberTotalCount / 100) +                               //Member ID Requests
                (memberTotalCount / 100) +                              //Member PR Requests
                (memberTotalCount) +                                    //Member Requests
                (repositoriesTotalCount / 100) +                        //Repositories Requests
                (memberTotalCount / 2) +                                //External Repo Requests
                (memberTotalCount) +                                    //Created Repos By Members Requests
                (teamTotalCount / 50) +                                 //Team Requests
                (2);                                                    //Organization Detail / Validation
    }
}
