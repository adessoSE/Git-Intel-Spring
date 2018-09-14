package requests;

import config.Config;
import enums.RequestType;
import objects.Query;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class ExternalRepoRequestTest {

    private ExternalRepoRequest externalRepoRequest;
    private String expectedGeneratedQueryContent;

    @Before
    public void setUp() throws Exception {
        this.externalRepoRequest = new ExternalRepoRequest("adessoAG", Arrays.asList("testRepoID"));
        this.expectedGeneratedQueryContent = "{\n" +
                //Request for ten repositories combined
                "nodes(ids: [" + this.externalRepoRequest.formatRepoIDs(Arrays.asList("testRepoID")) + "]) {\n" +
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
                "history(first: 50, since: \"" + this.externalRepoRequest.getDateToStartCrawlingInISO8601UTC(new Date()) + "\") {\n" +
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
        String generatedStartDateToCrawl = this.externalRepoRequest.getDateToStartCrawlingInISO8601UTC(calendar.getTime());
        assertEquals("2014-03-" + String.format("%02d", dateNumber - Config.PAST_DAYS_AMOUNT_TO_CRAWL) + "T00:00:00Z", generatedStartDateToCrawl);
    }

    @Test
    public void checkIfRepoIDsGetFormattedCorrectly() {
        String generatedFormattedRepoIDs = this.externalRepoRequest.formatRepoIDs(Arrays.asList("testRepoID", "anotherTestRepoID", "anotherTestRepoID"));
        assertEquals("\"testRepoID\",\"anotherTestRepoID\",\"anotherTestRepoID\",", generatedFormattedRepoIDs);
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.externalRepoRequest.generateQuery();
        assertEquals("adessoAG", query.getOrganizationName());
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.externalRepoRequest.generateQuery();
        assertEquals(RequestType.EXTERNAL_REPO, query.getQueryRequestType());
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.externalRepoRequest.generateQuery();
        assertEquals(this.expectedGeneratedQueryContent, query.getQuery());
    }
}