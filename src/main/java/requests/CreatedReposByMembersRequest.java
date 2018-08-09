package requests;

import enums.RequestType;
import enums.ResponseProcessor;
import objects.Query;

public class CreatedReposByMembersRequest extends Request {

    private String query;
    private ResponseProcessor responseProcessor;
    private RequestType requestType;
    private String organizationName;


    public CreatedReposByMembersRequest(String organizationName, String memberID, String endCursor) {
        this.organizationName = organizationName;
        this.query = "{\n" +
                "node(id: \"" + memberID + "\") {\n" +
                "... on User {\n" +
                "id\n" +
                "repositories(first: 100 after: " + endCursor + ") {\n" +
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
                "}";
        this.requestType = RequestType.CREATED_REPOS_BY_MEMBERS;
        this.responseProcessor = ResponseProcessor.CREATED_REPOS_BY_MEMBERS;
    }

    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.responseProcessor, this.requestType);
    }
}
