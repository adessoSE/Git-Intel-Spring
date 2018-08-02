package requests;

import config.Config;
import enums.RequestType;
import enums.ResponseProcessor;
import objects.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ExternalRepoRequest {

    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;
    private RequestType requestType;

    public ExternalRepoRequest(String organizationName, List<String> repoIDs) {
        this.organizationName = organizationName;
        this.query =               "{\n" +
                        "nodes(ids: [" + formatRepoIDs(repoIDs) + "]) {\n" +
                        "... on Repository {\n" +
                        "url\n" +
                        "id\n" +
                        "name\n" +
                        "description\n" +
                        "forkCount\n" +
                        "stargazers {\n" +
                        "totalCount\n" +
                        "}\n" +
                        "licenseInfo {\n" +
                        "name\n" +
                        "}\n" +
                        "primaryLanguage {\n" +
                        "name\n" +
                        "}\n" +
                        "defaultBranchRef {\n" +
                        "target {\n" +
                        "... on Commit {\n" +
                        "history(first: 50, since: \"" + getDateToStartCrawlingInISO8601UTC() + "\") {\n" +
                        "nodes {\n" +
                        "committedDate\n" +
                        "}\n" +
                        "}\n" +
                        "}\n" +
                        "}\n" +
                        "}\n" +
                        "pullRequests(last: 50) {\n" +
                        "nodes {\n" +
                        "createdAt\n" +
                        "}\n" +
                        "}\n" +
                        "issues(last: 50) {\n" +
                        "nodes {\n" +
                        "createdAt\n" +
                        "}\n" +
                        "}\n" +
                        "}\n" +
                        "}\n" +
                        "}";

        this.responseProcessor = ResponseProcessor.EXTERNAL_REPO;
        this.requestType = RequestType.EXTERNAL_REPO;
    }

    private String formatRepoIDs(List<String> repoIDs) {
        String formattedString = "";
        for (String repoID : repoIDs) {
            formattedString += "\"" + repoID.toString() + "\",";
        }
        return formattedString;
    }

    private String getDateToStartCrawlingInISO8601UTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date(System.currentTimeMillis() - Config.PAST_DAYS_TO_CRAWL_IN_MS));
    }

    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.responseProcessor, this.requestType);
    }
}
