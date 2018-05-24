package requests;

import entities.Level1.Data;
import enums.ResponseProcessor;
import objects.Query;

import java.util.Date;
import java.util.List;

public class MemberRequest extends Request {

    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;

    public MemberRequest(String organizationName, List<String> memberIDs) {
        this.organizationName = organizationName;
        this.query = "{\n" +
                "nodes(ids: ["+ formatMemberIDs(memberIDs) +"]) {\n" +
                "... on User {\n" +
                "name \n" +
                "login\n" +
                "url\n" +
                "avatarUrl\n" +
                "repositoriesContributedTo(last: 25, includeUserRepositories: true, contributionTypes: COMMIT) {\n" +
                "nodes {\n" +
                "defaultBranchRef {\n" +
                "target {\n" +
                "... on Commit {\n" +
                "history(first: 25, since: \"" + getDateWeekAgoInISO8601UTC() + "\") {\n" +
                "nodes {\n" +
                "committedDate\n" +
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
                "}\n" +
                "}\n" +
                "pullRequests(last: 25) {\n" +
                "nodes {\n" +
                "createdAt\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";

        this.responseProcessor = ResponseProcessor.MEMBER;
    }

    private String formatMemberIDs(List<String> memberIDs){
        String formattedString = "";
        for (String memberID : memberIDs){
            formattedString += "\"" + memberID.toString() + "\",";
        }
        return formattedString;
    }
    public Query crawlData() {
        Query requestQuery = new Query(this.organizationName, this.query, this.responseProcessor);
        Data response = this.crawlData(requestQuery).getData();
        System.out.println(response.getNodes().get(0).getName());
        requestQuery.setQueryResponse(response);
        return requestQuery;
    }
}
