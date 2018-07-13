package objects.Member;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberPR {

    private HashMap<String,ArrayList<String>> memberPRrepositoryIDs;
    private String endCursor;
    private boolean hasNextPage;

    /**
     * Wrapper-Object for the external pull requests of the members
     * @param memberPRrepositoryIDs HashMap - Keys as repository ID and value as list of contributor ID
     * @param endCursor Cursor of the last requested information. Required for processing the following information as starting point
     * @param hasNextPage Indicates if there is any information left to process after the request
     */
    public MemberPR(HashMap<String,ArrayList<String>> memberPRrepositoryIDs, String endCursor, boolean hasNextPage) {
        this.memberPRrepositoryIDs = memberPRrepositoryIDs;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public HashMap<String,ArrayList<String>> getMemberPRrepositoryIDs() {
        return memberPRrepositoryIDs;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
