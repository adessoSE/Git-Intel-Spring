package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;

/**
 * This is the request used for requesting the organization details.
 */
public class OrganizationDetailRequest {

    private final int estimatedQueryCost = 1;
    private String query;
    private String organizationName;
    private RequestType requestType;

    public OrganizationDetailRequest(String organizationName) {
        this.organizationName = organizationName;
        /**
         * GraphQL Request for the organization details.
         * Requesting the relevant information for the requested organization.
         * Requests the current rate limit of the token at the API.
         */
        this.query = "query {\n" +
                "organization(login:\"" + organizationName + "\") {\n" +
                "name \n" +
                "location \n" +
                "websiteUrl \n" +
                "url \n" +
                "avatarUrl \n" +
                "description \n" +
                "membersWithRole(first: 1) {\n" +
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
        this.requestType = RequestType.ORGANIZATION_DETAIL;

    }

    /**
     * Generates the query for the organizationDetail request.
     * @return Generated query for the request type.
     */
    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.requestType, this.estimatedQueryCost);
    }
}
