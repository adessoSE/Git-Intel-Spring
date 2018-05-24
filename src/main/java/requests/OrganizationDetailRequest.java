package requests;

import enums.ResponseProcessor;
import objects.Query;

public class OrganizationDetailRequest extends Request {

    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;

    public OrganizationDetailRequest(String organizationName) {
        this.organizationName = organizationName;
        this.query = "query {\n" +
                "  organization(login:\"" + organizationName + "\") {\n" +
                " name \n" +
                " location \n" +
                " websiteUrl \n" +
                " url \n" +
                " description \n" +
                "    members(first: 1) {\n" +
                "      totalCount\n" +
                "    }\n" +
                "    teams(first: 1) {\n" +
                "      totalCount\n" +
                "    }\n" +
                "    repositories(first: 1) {\n" +
                "      totalCount\n" +
                "    }\n" +
                "  }\n" +
                "}";
        this.responseProcessor = ResponseProcessor.ORGANIZATION_DETAIL;
    }

    public Query crawlData() {
        Query requestQuery = new Query(this.organizationName, this.query, this.responseProcessor);
        requestQuery.setQueryResponse(this.crawlData(requestQuery).getData());
        return requestQuery;
    }
}
