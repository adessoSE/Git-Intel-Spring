package processors;

import objects.Query;
import objects.ResponseWrapper;

public class OrganizationValidationProcessor {

    private Query requestQuery;

    public OrganizationValidationProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        boolean isValidOrganization = false;
        if (this.requestQuery.getQueryResponse().getResponseOrganization().getData().getOrganization() != null) {
            isValidOrganization = true;
        }
        return new ResponseWrapper(isValidOrganization);
    }
}
