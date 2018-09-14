package requests;

import enums.RequestType;
import objects.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MemberPRRequestTest {

    private MemberPRRequest memberPRRequest;
    private String expectedGeneratedQueryContent = "query {\n" +
            "organization(login:\"adessoAG\") {\n" +
            "members(first: 100, after: testEndCursor) {\n" +
            "pageInfo {\n" +
            "hasNextPage\n" +
            "endCursor\n" +
            "}\n" +
            "nodes {\n" +
            "id\n" +
            "pullRequests(last: 25, states: [MERGED, OPEN]) {\n" +
            "nodes {\n" +
            "updatedAt \n" +
            "repository {\n" +
            "id\n" +
            "isFork\n" +
            "}\n" +
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
        this.memberPRRequest = new MemberPRRequest("adessoAG", "testEndCursor");
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.memberPRRequest.generateQuery();
        assertEquals("adessoAG", query.getOrganizationName());
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.memberPRRequest.generateQuery();
        assertEquals(RequestType.MEMBER_PR, query.getQueryRequestType());
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.memberPRRequest.generateQuery();
        assertEquals(this.expectedGeneratedQueryContent, query.getQuery());
    }
}