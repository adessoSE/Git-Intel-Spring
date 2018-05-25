package Tasks;

import enums.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import repositories.RequestRepository;
import requests.RequestManager;

public class OrganisationUpdateTask {

    @Autowired
    RequestRepository requestRepository;

    @Scheduled(fixedRate = 12000)
    private void generateQuery() {
        requestRepository.save(new RequestManager("adessoAG").processRequest(RequestType.MEMBER_ID));
        requestRepository.save(new RequestManager("adessoAG").processRequest(RequestType.ORGANIZATION_DETAIL));
    }
}
