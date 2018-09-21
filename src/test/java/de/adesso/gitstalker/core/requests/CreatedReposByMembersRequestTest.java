package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreatedReposByMembersRequestTest {

    private CreatedReposByMembersRequest createdReposByMembersRequest;
    private String expectedGeneratedQueryContent;

    @Before
    public void setUp() throws Exception {
        this.createdReposByMembersRequest = new CreatedReposByMembersRequest("adessoAG", "testMemberID", "testEndCursor");
        this.expectedGeneratedQueryContent = "{\n" +
                "node(id: \"testMemberID\") {\n" +
                "... on User {\n" +
                "id\n" +
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
                "isFork\n" +
                "isMirror\n" +
                "owner {\n" +
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
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.createdReposByMembersRequest.generateQuery();
        assertEquals("adessoAG", query.getOrganizationName());
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.createdReposByMembersRequest.generateQuery();
        assertEquals(RequestType.CREATED_REPOS_BY_MEMBERS, query.getQueryRequestType());
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.createdReposByMembersRequest.generateQuery();
        assertEquals(this.expectedGeneratedQueryContent, query.getQuery());
    }
}