package requests;

import enums.RequestType;
import objects.Query;

public class OrganizationValidationRequest {

    private final int estimatedQueryCost = 1;
    private String query;
    private String organizationName;
    private RequestType requestType;

    public OrganizationValidationRequest(String organizationName) {
        this.organizationName = organizationName;
        this.query = "query {\n" +
                "organization(login:\"" + organizationName + "\") {\n" +
                "id\n" +
                "}\n" +
                "rateLimit {\n" +
                "cost\n" +
                "remaining\n" +
                "resetAt\n" +
                "}\n" +
                "}";
        this.requestType = RequestType.ORGANIZATION_VALIDATION;

    }

    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.requestType, this.estimatedQueryCost);
    }
}
