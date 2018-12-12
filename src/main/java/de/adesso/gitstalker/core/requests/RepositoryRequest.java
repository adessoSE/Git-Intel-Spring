package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.config.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This is the request used for requesting the repositories.
 */
public class RepositoryRequest {

    private final int estimatedQueryCost = 2;
    private String query;
    private String organizationName;
    private RequestType requestType;

    public RepositoryRequest(String organizationName, String endCursor) {
        this.organizationName = organizationName;
        /**
         * GraphQL Request for the repositories of the organization.
         * Requesting information for the first 100 repositories of the organization. Checks if there is information left with the pageInfo.
         * For each repository it's collecting relevant information, the last 50 commits since a date, the last 50 issues and the last 50 pull requests.
         * Requests the current rate limit of the token at the API.
         */
        this.query = "query {\n" +
                "organization(login: \"" + organizationName + "\") {\n" +
                "repositories(first: 100 after: " + endCursor + ") {\n" +
                "pageInfo {\n" +
                "hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
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
                "history(first: 50, since: \"" + this.getDateToStartCrawlingInISO8601UTC(new Date()) + "\") {\n" +
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
                "}\n" +
                "rateLimit {\n" +
                "cost\n" +
                "remaining\n" +
                "resetAt\n" +
                "}\n" +
                "}";
        this.requestType = RequestType.REPOSITORY;
    }

    /**
     * Generates the query for the repositories request.
     * @return Generated query for the request type.
     */
    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.requestType, this.estimatedQueryCost);
    }

    /**
     * Formats the date to fit the API
     * @param currentDate Unformatted date
     * @return Formatted Date as String
     */
    protected String getDateToStartCrawlingInISO8601UTC(Date currentDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return df.format(new Date(currentDate.getTime() - Config.PAST_DAYS_TO_CRAWL_IN_MS));
    }
}
