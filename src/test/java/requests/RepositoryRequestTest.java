package requests;

import enums.RequestType;
import objects.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RepositoryRequestTest {

    private RepositoryRequest repositoryRequest;
    private String generatedQueryContent;

    @Before
    public void setUp() throws Exception {
        this.repositoryRequest = new RepositoryRequest("adessoAG", "testEndCursor");
        this.generatedQueryContent = "query {\n" +
                "organization(login: \"adessoAG\") {\n" +
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
                "defaultBranchRef {\n" +
                "target {\n" +
                "... on Commit {\n" +
                "history(first: 50, since: \"" + this.repositoryRequest.getDateToStartCrawlingInISO8601UTC() + "\") {\n" +
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
        Query query = this.repositoryRequest.generateQuery();
        assertEquals(query.getOrganizationName(), "adessoAG");
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.repositoryRequest.generateQuery();
        assertEquals(query.getQueryRequestType(), RequestType.REPOSITORY);
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.repositoryRequest.generateQuery();
        assertEquals(query.getQuery(), this.generatedQueryContent);
    }
}