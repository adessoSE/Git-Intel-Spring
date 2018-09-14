package requests;

import enums.RequestType;
import objects.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MemberIDRequestTest {

    private MemberIDRequest memberIDRequest;
    private String expectedGeneratedQueryContent;

    @Before
    public void setUp() throws Exception {
        this.memberIDRequest = new MemberIDRequest("adessoAG", "testEndCursor");
        this.expectedGeneratedQueryContent = "query {\n" +
                "organization(login: \"adessoAG\") {\n" +
                "members(first: 100 after: testEndCursor) {\n" +
                "pageInfo {\n" +
                " hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
                "id\n" +
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
        assertEquals(this.expectedGeneratedQueryContent, query.getQuery());
    }
}