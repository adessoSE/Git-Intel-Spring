package objects.Member;

import java.util.ArrayList;

public class MemberID {

    private ArrayList<String> memberIDs;
    private String endCursor;
    private boolean hasNextPage;

    /**
     * Wrapper-Object for storing the collected MemberIDs and information about the remaining member amount
     * @param memberIDs Storing of the collected MemberIDs out of the request
     * @param endCursor Cursor of the last requested information. Required for processing the following information as starting point
     * @param hasNextPage Indicates if there is any information left to process after the request
     */
    public MemberID(ArrayList<String> memberIDs, String endCursor, boolean hasNextPage) {
        this.memberIDs = memberIDs;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public ArrayList<String> getMemberIDs() {
        return memberIDs;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
