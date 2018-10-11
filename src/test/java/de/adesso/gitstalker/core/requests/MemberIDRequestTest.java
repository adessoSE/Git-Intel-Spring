package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.resources.MemberIDResources;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MemberIDRequestTest {

    private MemberIDRequest memberIDRequest;
    private MemberIDResources memberIDResources;

    @Before
    public void setUp() throws Exception {
        this.memberIDRequest = new MemberIDRequest("adessoAG", "testEndCursor");
        this.memberIDResources = new MemberIDResources();
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.memberIDRequest.generateQuery();
        assertEquals("adessoAG", query.getOrganizationName());
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.memberIDRequest.generateQuery();
        assertEquals(RequestType.MEMBER_ID, query.getQueryRequestType());
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.memberIDRequest.generateQuery();
        assertEquals(this.memberIDResources.getExpectedGeneratedQueryContent(), query.getQueryContent());
    }
}