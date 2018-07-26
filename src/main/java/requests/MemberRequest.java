package requests;

import enums.RequestType;
import enums.ResponseProcessor;
import objects.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MemberRequest {

    private String query;
    private ResponseProcessor responseProcessor;
    private String organizationName;
    private RequestType requestType;

    public MemberRequest(String organizationName, List<String> memberIDs) {
        this.organizationName = organizationName;
        this.query = "{\n" +
                "nodes(ids: [" + formatMemberIDs(memberIDs) + "]) {\n" +
                "... on User {\n" +
                "name \n" +
                "id \n" +
                "login\n" +
                "url\n" +
                "avatarUrl\n" +
                "repositoriesContributedTo(last: 25, includeUserRepositories: true, contributionTypes: COMMIT) {\n" +
                "nodes {\n" +
                "id \n" +
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
        this.requestType = RequestType.MEMBER;
    }

    private String formatMemberIDs(List<String> memberIDs) {
        String formattedString = "";
        for (String memberID : memberIDs) {
            formattedString += "\"" + memberID.toString() + "\",";
        }
        return formattedString;
    }

    private String getDateWeekAgoInISO8601UTC() {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date(System.currentTimeMillis() - (7 * DAY_IN_MS)));
    }

    public Query generateQuery() {
        return new Query(this.organizationName, this.query, this.responseProcessor, this.requestType);
    }
}
