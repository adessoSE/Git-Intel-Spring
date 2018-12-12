package de.adesso.gitstalker.core.Tasks;

import de.adesso.gitstalker.core.REST.OrganizationController;
import de.adesso.gitstalker.core.REST.responses.ProcessingOrganization;
import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.processors.ProcessingInformationProcessor;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;

import java.util.Date;

public class OrganizationUpdateTask {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ProcessingRepository processingRepository;

    /**
     * Scheduled task for generating queries in order to update saved organisation. Preparing the organisation for the update.
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    private void generateQuery() {
        for (OrganizationWrapper wrapper : organizationRepository.findAllByLastUpdateTimestampLessThanEqual(new Date(System.currentTimeMillis() - Config.UPDATE_RATE_DAYS_IN_MS))){
            wrapper.prepareOrganizationForUpdate(organizationRepository);
            ProcessingInformationProcessor processingInformationProcessor = new ProcessingInformationProcessor(wrapper.getOrganizationName(), processingRepository, organizationRepository, requestRepository);
            processingInformationProcessor.getOrganizationValidationResponse();
            processingInformationProcessor.addProcessingOrganizationInformationIfMissingForTheOrganization();
        }
    }
}
