package objects;

import java.util.ArrayList;
import java.util.Set;

public class MemberID {

    private Set<String> memberIDs;
    private String endCursor;
    private boolean hasNextPage;

    public MemberID(Set<String> memberIDs, String endCursor, boolean hasNextPage) {
        this.memberIDs = memberIDs;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public Set<String> getMemberIDs() {
        return memberIDs;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
