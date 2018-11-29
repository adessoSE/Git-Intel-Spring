package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.enums.RequestType;

/**
 * This is the request used for requesting MemberPR.
 */
public class MemberPRRequest {

    private final int estimatedQueryCost = 1;
    private String query;
    private RequestType requestType;
    private String organizationName;

    public MemberPRRequest(String organizationName, String endCursor) {
        this.organizationName = organizationName;
        /**
         * GraphQL Request for the member pull requests.
         * Requests of the organization the first 100 members. Checks if there are information left with pageInfo and gets the last 25 pull requests of each member.
         * Requests the current rate limit of the token at the API.
         */
        this.query = "query {\n" +
                "organization(login:\"" + organizationName + "\") {\n" +
                "members(first: 100, after: " + endCursor + ") {\n" +
                "pageInfo {\n" +
                "hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
                "id\n" +
                "pullRequests(last: 25, states: [MERGED, OPEN]) {\n" +
                "nodes {\n" +
                "updatedAt \n" +
                "repository {\n" +
                "id\n" +
                "isFork\n" +
                "}\n" +
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
        this.requestType = RequestType.MEMBER_PR;
    }

    /**
     * Generates the query for the member pull-requests request.
     * @return Generated query for the request type.
     */
    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.requestType, this.estimatedQueryCost);
    }
}
