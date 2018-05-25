package objects;

import java.util.ArrayList;

public class MemberPR {

    private ArrayList<String> memberPRrepositoryIDs;
    private String endCursor;
    private boolean hasNextPage;

    public MemberPR(ArrayList<String> memberPRrepositoryIDs, String endCursor, boolean hasNextPage) {
        this.memberPRrepositoryIDs = memberPRrepositoryIDs;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public ArrayList<String> getMemberPRrepositoryIDs() {
        return memberPRrepositoryIDs;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
