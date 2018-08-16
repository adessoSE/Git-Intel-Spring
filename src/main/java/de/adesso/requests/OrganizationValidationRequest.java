package de.adesso.requests;

import de.adesso.enums.RequestType;
import de.adesso.enums.ResponseProcessor;
import de.adesso.objects.Query;

public class OrganizationValidationRequest {

    private final int estimatedQueryCost = 1;
    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;
    private RequestType requestType;

    public OrganizationValidationRequest(String organizationName) {
        this.organizationName = organizationName;
        this.query = "query {\n" +
                "  organization(login:\"" + organizationName + "\") {\n" +
                "    id\n" +
                "  }\n" +
                "}";
        this.responseProcessor = ResponseProcessor.ORGANIZATION_VALIDATION;
        this.requestType = RequestType.ORGANIZATION_VALIDATION;

    }

    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.responseProcessor, this.requestType,this.estimatedQueryCost);
    }
}
