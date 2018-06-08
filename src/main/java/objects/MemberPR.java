package objects;

import java.util.Set;

public class MemberPR {

    private Set<String> memberPRrepositoryIDs;
    private String endCursor;
    private boolean hasNextPage;

    public MemberPR(Set<String> memberPRrepositoryIDs, String endCursor, boolean hasNextPage) {
        this.memberPRrepositoryIDs = memberPRrepositoryIDs;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public Set<String> getMemberPRrepositoryIDs() {
        return memberPRrepositoryIDs;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
