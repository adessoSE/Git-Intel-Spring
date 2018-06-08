package objects;

import java.util.ArrayList;

public class Repositories {

    private ArrayList<Repository> repositories;
    private String endCursor;
    private boolean hasNextPage;

    public Repositories(ArrayList<Repository> repositories, String endCursor, boolean hasNextPage) {
        this.repositories = repositories;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public ArrayList<Repository> getRepositories() {
        return repositories;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

}
