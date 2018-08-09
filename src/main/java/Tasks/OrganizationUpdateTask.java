package Tasks;

import enums.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import repositories.RequestRepository;
import requests.RequestManager;

public class OrganizationUpdateTask {


    @Autowired
    RequestRepository requestRepository;

    /**
     * Scheduled task for generating queries in order to update saved organisation.
     * Currently used for debugging the requests.
     * TODO: Scheduled task should run at night time!
     * TODO: Request have to be done in specific sequence. MEMBER_PR needs REPOSITORY first. Define in RequestTask
     */
    @Scheduled(fixedDelay = 300000000, initialDelay = 5000)
    private void generateQuery() {
//        requestRepository.save(new RequestManager("adessoAGg").generateRequest(RequestType.ORGANIZATION_VALIDATION));
    }
}
