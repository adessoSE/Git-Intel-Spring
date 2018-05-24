package requests;

import enums.ResponseProcessor;
import objects.Query;

public class MemberIDRequest extends Request {

    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;

    public MemberIDRequest(String organizationName, String endCursor) {
        this.organizationName = organizationName;
        this.query = "query {\n" +
                "organization(login: \"" + organizationName + "\") {\n" +
                "members(first: 100 after:" + endCursor + ") {\n" +
                "pageInfo {\n" +
                " hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
                "id\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";

        this.responseProcessor = ResponseProcessor.MEMBER_ID;
    }

    public Query crawlData() {
        Query requestQuery = new Query(this.organizationName, this.query, this.responseProcessor);
        requestQuery.setQueryResponse(this.crawlData(requestQuery).getData());
        return requestQuery;
    }
}
