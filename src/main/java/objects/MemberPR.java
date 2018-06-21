package objects;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberPR {

    private HashMap<String,ArrayList<String>> memberPRrepositoryIDs;
    private String endCursor;
    private boolean hasNextPage;

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
