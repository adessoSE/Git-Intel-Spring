package processors;

import objects.OrganizationWrapper;
import objects.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import requests.RequestManager;

@Service
@Transactional
public class OrganizationValidationProcessor extends ResponseProcessor {

    private Query requestQuery;


    public OrganizationValidationProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public void processResponse(Query requestQuery) {
        if (this.requestQuery.getQueryResponse().getResponseOrganization().getData().getOrganization() != null) {
            super.test();
            requestRepository.saveAll(new RequestManager(requestQuery.getOrganizationName()).generateAllRequests());
            OrganizationWrapper organization = new OrganizationWrapper(requestQuery.getOrganizationName());
            organizationRepository.save(organization);
        }

    }
}
