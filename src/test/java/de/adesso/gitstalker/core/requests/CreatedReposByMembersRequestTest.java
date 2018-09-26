package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.resources.CreatedReposByMemberResources;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreatedReposByMembersRequestTest {

    private CreatedReposByMembersRequest createdReposByMembersRequest;
    private CreatedReposByMemberResources createdReposByMemberResources;

    @Before
    public void setUp() throws Exception {
        this.createdReposByMemberResources = new CreatedReposByMemberResources();
        this.createdReposByMembersRequest = new CreatedReposByMembersRequest("adessoAG", "testMemberID", "testEndCursor");
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
        assertEquals(this.createdReposByMemberResources.getExpectedGeneratedQueryContent(), query.getQuery());
    }
}