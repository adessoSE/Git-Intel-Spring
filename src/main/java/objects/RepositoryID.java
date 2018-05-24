package objects;

import java.util.ArrayList;

public class RepositoryID {

    private ArrayList<String> repositoryIDs;
    private String endCursor;
    private boolean hasNextPage;

    public RepositoryID(ArrayList<String> repositoryIDs, String endCursor, boolean hasNextPage) {
        this.repositoryIDs = repositoryIDs;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public ArrayList<String> getRepositoryIDs() {
        return repositoryIDs;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
