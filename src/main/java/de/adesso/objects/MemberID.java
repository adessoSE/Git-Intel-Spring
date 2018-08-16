package objects;

import java.util.ArrayList;

public class MemberID {

    private ArrayList<String> memberIDs;
    private String endCursor;
    private boolean hasNextPage;

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
