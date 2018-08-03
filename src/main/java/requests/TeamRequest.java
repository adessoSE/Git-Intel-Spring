package requests;

import config.Config;
import enums.RequestType;
import enums.ResponseProcessor;
import objects.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TeamRequest extends Request {

    private final int estimatedQueryCost = 5;
    private String query;
    private ResponseProcessor responseProcessor;
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
                "repositories(first: 10) {\n" +
                "totalCount\n" +
                "nodes {\n" +
                "name\n" +
                "defaultBranchRef {\n" +
                "target {\n" +
                "... on Commit {\n" +
                "history(first: 25, since: \"" + getDateToStartCrawlingInISO8601UTC() + "\") {\n" +
                "totalCount\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "members {\n" +
                "totalCount\n" +
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

        this.responseProcessor = ResponseProcessor.TEAM;
        this.requestType = RequestType.TEAM;
    }

    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.responseProcessor, this.requestType, this.estimatedQueryCost);
    }

    private String getDateToStartCrawlingInISO8601UTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date(System.currentTimeMillis() - Config.PAST_DAYS_TO_CRAWL_IN_MS));
    }
}
