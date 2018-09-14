package requests;

import enums.RequestType;
import objects.Query;
import org.junit.Before;
import org.junit.Test;

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
                "history(first: 100, since: \"" + this.memberRequest.getDateToStartCrawlingInISO8601UTC() + "\"  ,author: {id: \"testMemberID\"}) {\n" +
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
        assertEquals(this.expectedGeneratedQueryContent, query.getQuery());
    }
}