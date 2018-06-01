package objects;

import java.util.ArrayList;
import java.util.Set;

public class RepositoryID {

    private Set<String> repositoryIDs;
    private String endCursor;
    private boolean hasNextPage;

    public RepositoryID(Set<String> repositoryIDs, String endCursor, boolean hasNextPage) {
        this.repositoryIDs = repositoryIDs;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public Set<String> getRepositoryIDs() {
        return repositoryIDs;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
