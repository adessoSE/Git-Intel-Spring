package requests;

import enums.RequestType;
import objects.Query;

public class TeamRequest extends Request {

    private final int estimatedQueryCost = 1;
    private String query;
    private RequestType requestType;
    private String organizationName;

    public TeamRequest(String organizationName, String endCursor) {
        this.organizationName = organizationName;
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

    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.requestType, this.estimatedQueryCost);
    }

}
