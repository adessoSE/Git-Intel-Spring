package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TeamRequestTest {

    private TeamRequest teamRequest;
    private String expectedGeneratedQueryContent = "query {\n" +
            "organization(login: \"adessoAG\") {\n" +
            "teams(first: 50, after: testEndCursor) {\n" +
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
            "repositories (first: 100) {\n" +
            "nodes {\n" +
            "id\n" +
            "}\n" +
            "}\n" +
            "members (first: 100) {\n" +
            "totalCount\n" +
            "nodes {\n" +
            "id\n" +
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

    @Before
    public void setUp() throws Exception {
        this.teamRequest = new TeamRequest("adessoAG", "testEndCursor");
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.teamRequest.generateQuery();
        assertEquals("adessoAG", query.getOrganizationName());
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.teamRequest.generateQuery();
        assertEquals(RequestType.TEAM, query.getQueryRequestType());
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.teamRequest.generateQuery();
        assertEquals(this.expectedGeneratedQueryContent, query.getQuery());
    }
}