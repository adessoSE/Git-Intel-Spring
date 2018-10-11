package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.resources.ExternalRepoResources;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class ExternalRepoRequestTest {

    private ExternalRepoRequest externalRepoRequest;
    private ExternalRepoResources externalRepoResources;

    @Before
    public void setUp() throws Exception {
        this.externalRepoRequest = new ExternalRepoRequest("adessoAG", Arrays.asList("testRepoID"));
        this.externalRepoResources = new ExternalRepoResources();
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
        assertEquals(this.externalRepoResources.getExpectedGeneratedQueryContent(this.externalRepoRequest.formatRepoIDs(Arrays.asList("testRepoID")), this.externalRepoRequest.getDateToStartCrawlingInISO8601UTC(new Date())), query.getQuery());
    }
}