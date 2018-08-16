package objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Repositories {

    private HashMap<String,Repository> repositories;
    private String endCursor;
    private boolean hasNextPage;

    public Repositories(HashMap<String,Repository> repositories){
        this.repositories = repositories;
    }

    public Repositories(HashMap<String,Repository> repositories, String endCursor, boolean hasNextPage) {
        this.repositories = repositories;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public HashMap<String,Repository> getRepositories() {
        return repositories;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

}
