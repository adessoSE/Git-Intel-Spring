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

public class MemberRequestTest {

    private MemberRequest memberRequest;
    private String expectedGeneratedQueryContent;

    @Before
    public void setUp() throws Exception {
        this.memberRequest = new MemberRequest("adessoAG", "testMemberID");
        this.expectedGeneratedQueryContent = "{\n" +
                "node(id: \"testMemberID\") {\n" +
                "... on User {\n" +
                "name\n" +
                "id\n" +
                "login\n" +
                "url\n" +
                "avatarUrl\n" +
                "repositoriesContributedTo(first: 100, contributionTypes: COMMIT, includeUserRepositories: true) {\n" +
                "nodes {\n" +
                "id\n" +
                "defaultBranchRef {\n" +
                "target {\n" +
                "... on Commit {\n" +
                "history(first: 100, since: \"" + this.memberRequest.getDateToStartCrawlingInISO8601UTC(new Date()) + "\"  ,author: {id: \"testMemberID\"}) {\n" +
                "nodes {\n" +
                "committedDate\n" +
                "url\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "issues(last: 25) {\n" +
                "nodes {\n" +
                "createdAt\n" +
                "url\n" +
                "}\n" +
                "}\n" +
                "pullRequests(last: 25) {\n" +
                "nodes {\n" +
                "createdAt\n" +
                "url\n" +
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
        String generatedStartDateToCrawl = this.memberRequest.getDateToStartCrawlingInISO8601UTC(calendar.getTime());
        assertEquals("2014-03-" + String.format("%02d", dateNumber - Config.PAST_DAYS_AMOUNT_TO_CRAWL) + "T00:00:00Z", generatedStartDateToCrawl);
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.memberRequest.generateQuery();
        assertEquals("adessoAG", query.getOrganizationName());
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.memberRequest.generateQuery();
        assertEquals(RequestType.MEMBER, query.getQueryRequestType());
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.memberRequest.generateQuery();
        assertEquals(this.expectedGeneratedQueryContent, query.getQueryContent());
    }
}