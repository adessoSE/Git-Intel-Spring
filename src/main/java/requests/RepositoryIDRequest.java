package requests;

import enums.ResponseProcessor;
import objects.Query;

public class RepositoryIDRequest extends Request{

    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;

    public RepositoryIDRequest(String organizationName, String endCursor) {
        this.organizationName = organizationName;
        this.query = "query {\n" +
                "organization(login: \"" + organizationName + "\") {\n" +
                "repositories(first: 100, after: " + endCursor + ") {\n" +
                "pageInfo {\n" +
                "hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
                "id\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";

        this.responseProcessor = ResponseProcessor.REPOSITORY_ID;
    }

    public Query crawlData() {
        Query requestQuery = new Query(this.organizationName, this.query, this.responseProcessor);
        requestQuery.setQueryResponse(this.crawlData(requestQuery).getData());
        return requestQuery;
    }
}
