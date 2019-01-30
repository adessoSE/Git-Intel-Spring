package de.adesso.gitstalker.core.Tasks;

import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.processors.ProcessingInformationProcessor;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrganizationUpdateTask {

    @Transient
    private Logger logger = LoggerFactory.getLogger(OrganizationUpdateTask.class);

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ProcessingRepository processingRepository;

    /**
     * Scheduled task for generating queries in order to update saved organisation. Preparing the organisation for the update.
     */
    @Scheduled(fixedDelay = Config.PROCESSING_RATE_IN_MS, initialDelay = Config.PROCESSING_DELAY_IN_MS)
    private void generateQuery() {
        ArrayList<OrganizationWrapper> updatableOrganizations = organizationRepository.findAllByLastUpdateTimestampBefore(new Date(System.currentTimeMillis() - Config.UPDATE_RATE_DAYS_IN_MS));
        this.startUpdateForOrganizations(this.filterOrganizationsBasedOnLastAccess(updatableOrganizations));
    }

    private void startUpdateForOrganizations(List<OrganizationWrapper> updatableOrganizations){
        for (OrganizationWrapper wrapper : updatableOrganizations) {
            logger.info("Started Update for organisation: " + wrapper.getOrganizationName());
            wrapper.prepareOrganizationForUpdate(organizationRepository);
            ProcessingInformationProcessor processingInformationProcessor = new ProcessingInformationProcessor(wrapper.getOrganizationName(), processingRepository, organizationRepository, requestRepository);
            processingInformationProcessor.getOrganizationValidationResponse();
            processingInformationProcessor.addProcessingOrganizationInformationIfMissingForTheOrganization();
        }
    }

    private List<OrganizationWrapper> filterOrganizationsBasedOnLastAccess(ArrayList<OrganizationWrapper> updatableOrganizations){
        List<OrganizationWrapper> filteredOrganizations = updatableOrganizations.stream().filter(organizationWrapper ->
                organizationWrapper.getLastAccessTimestamp().after(new Date(System.currentTimeMillis() - Config.LIMIT_BEFORE_LAST_ACCESS_DATE_IN_MS)))
                .collect(Collectors.toList());
        return filteredOrganizations;
    }
}
