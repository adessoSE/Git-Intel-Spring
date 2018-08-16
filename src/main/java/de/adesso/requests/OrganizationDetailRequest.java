package requests;

import enums.RequestType;
import enums.ResponseProcessor;
import objects.Query;

public class OrganizationDetailRequest {

    private final int estimatedQueryCost = 1;
    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;
    private RequestType requestType;

    public OrganizationDetailRequest(String organizationName) {
        this.organizationName = organizationName;
        this.query = "query {\n" +
                "organization(login:\"" + organizationName + "\") {\n" +
                "name \n" +
                "location \n" +
                "websiteUrl \n" +
                "url \n" +
                "avatarUrl \n" +
                "description \n" +
                "members(first: 1) {\n" +
                "totalCount\n" +
                "}\n" +
                "teams(first: 1) {\n" +
                "totalCount\n" +
                "}\n" +
                "repositories(first: 1) {\n" +
                "totalCount\n" +
                "}\n" +
                "}\n" +
                "rateLimit {\n" +
                "cost\n" +
                "remaining\n" +
                "resetAt\n" +
                "}\n" +
                "}";
        this.responseProcessor = ResponseProcessor.ORGANIZATION_DETAIL;
        this.requestType = RequestType.ORGANIZATION_DETAIL;

    }

    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.responseProcessor, this.requestType, this.estimatedQueryCost);
    }
}
