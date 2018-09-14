package de.adesso.gitstalker.core.Tasks;

import de.adesso.gitstalker.core.enums.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;

public class OrganizationUpdateTask {


    @Autowired
    RequestRepository requestRepository;

    /**
     * Scheduled task for generating queries in order to update saved organisation.
     * Currently used for debugging the de.adesso.gitstalker.core.requests.
     * TODO: Scheduled task should run at night time!
     */
    @Scheduled(fixedDelay = 300000000, initialDelay = 5000)
    private void generateQuery() {
       requestRepository.save(new RequestManager("adessoAG").generateRequest(RequestType.ORGANIZATION_VALIDATION));
    }
}
