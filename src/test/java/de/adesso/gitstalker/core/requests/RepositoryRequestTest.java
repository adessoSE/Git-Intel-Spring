package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class RepositoryRequestTest {

    private RepositoryRequest repositoryRequest;
    private String expectedGeneratedQueryContent;

    @Before
    public void setUp() throws Exception {
        this.repositoryRequest = new RepositoryRequest("adessoAG", "testEndCursor");
        this.expectedGeneratedQueryContent = "query {\n" +
                "organization(login: \"adessoAG\") {\n" +
                "repositories(first: 100 after: testEndCursor) {\n" +
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
                "history(first: 50, since: \"" + this.repositoryRequest.getDateToStartCrawlingInISO8601UTC(new Date()) + "\") {\n" +
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
    }

    @Test
    public void checkIfDateToStartCrawlingIsGeneratedCorrectly() {
        int dateNumber = 30;
        Calendar calendar = new GregorianCalendar(2014, 2, dateNumber);
        String generatedStartDateToCrawl = this.repositoryRequest.getDateToStartCrawlingInISO8601UTC(calendar.getTime());
        assertEquals("2014-03-" + String.format("%02d", dateNumber - Config.PAST_DAYS_AMOUNT_TO_CRAWL) + "T00:00:00Z", generatedStartDateToCrawl);
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.repositoryRequest.generateQuery();
        assertEquals("adessoAG", query.getOrganizationName());
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.repositoryRequest.generateQuery();
        assertEquals(RequestType.REPOSITORY, query.getQueryRequestType());
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.repositoryRequest.generateQuery();
        assertEquals(this.expectedGeneratedQueryContent, query.getQuery());
    }
}