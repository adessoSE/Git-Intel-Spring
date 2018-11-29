package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.enums.RequestType;

/**
 * This is the request used for requesting the teams of the organization.
 */
public class TeamRequest extends Request {

    private final int estimatedQueryCost = 1;
    private String query;
    private RequestType requestType;
    private String organizationName;

    public TeamRequest(String organizationName, String endCursor) {
        this.organizationName = organizationName;
        /**
         * GraphQL Request for the repositories of the organization.
         * Requesting information for the first 50 teams of the organization. Checks if there is information left with the pageInfo.
         * For each team it's collecting relevant information, the first 100 repositories and the first 100 members.
         * Requests the current rate limit of the token at the API.
         */
        this.query = "query {\n" +
                "organization(login: \"" + organizationName + "\") {\n" +
                "teams(first: 50, after: " + endCursor + ") {\n" +
                "pageInfo {\n" +
                "hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "totalCount\n" +
                "nodes {\n" +
                "name\n" +
                "id\n" +
                "description\n" +
                "avatarUrl\n" +
                "url\n" +
                "repositories (first: 100) {\n" +
                "nodes {\n" +
                "id\n" +
                "}\n" +
                "}\n" +
                "members (first: 100) {\n" +
                "totalCount\n" +
                "nodes {\n" +
                "id\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "rateLimit {\n" +
                "cost\n" +
                "remaining\n" +
                "resetAt\n" +
                "}\n" +
                "}";
        this.requestType = RequestType.TEAM;
    }

    /**
     * Generates the query for the team request.
     * @return Generated query for the request type.
     */
    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.requestType, this.estimatedQueryCost);
    }

}
