package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;

/**
 * This is the request used for requesting MemberID.
 */
public class MemberIDRequest extends Request {

    private final int estimatedQueryCost = 1;
    private String query;
    private RequestType requestType;
    private String organizationName;

    public MemberIDRequest(String organizationName, String endCursor) {
        this.organizationName = organizationName;
        /**
         * GraphQL Request for the member IDs
         * Requests the first 100 members of the organization. Checks if there are information left with pageInfo and gets the id of each member.
         * Requests the current rate limit of the token at the API.
         */
        this.query = "query {\n" +
                "organization(login: \"" + organizationName + "\") {\n" +
                "membersWithRole(first: 100 after: " + endCursor + ") {\n" +
                "pageInfo {\n" +
                "hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
                "id\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "rateLimit {\n" +
                "cost\n" +
                "remaining\n" +
                "resetAt\n" +
                "}\n" +
                "}";
        this.requestType = RequestType.MEMBER_ID;
    }

    /**
     * Generates the query for the MemberID request.
     * @return Generated query for the request type.
     */
    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.requestType, this.estimatedQueryCost);
    }
}
