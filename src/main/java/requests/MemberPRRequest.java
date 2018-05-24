package requests;

import enums.ResponseProcessor;
import objects.Query;

public class MemberPRRequest extends Request {

    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;

    public MemberPRRequest(String organizationName, String endCursor) {
        this.organizationName = organizationName;
        this.query = "query {\n" +
                "organization(login:\"" + organizationName + "\") {\n" +
                "members(first: 100, after: " + endCursor + ") {\n" +
                "pageInfo {\n" +
                "hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
                "pullRequests(last: 25, states: [MERGED, OPEN]) {\n" +
                "nodes {\n" +
                "repository {\n" +
                "id\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";

        this.responseProcessor = ResponseProcessor.MEMBER_PR;
    }

    public Query crawlData() {
        Query requestQuery = new Query(this.organizationName, this.query, this.responseProcessor);
        requestQuery.setQueryResponse(this.crawlData(requestQuery).getData());
        return requestQuery;
    }
}
