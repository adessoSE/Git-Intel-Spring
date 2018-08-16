package objects;

import java.util.ArrayList;
import java.util.HashMap;

public class CreatedRepositoriesByMembers {

    private HashMap<String, ArrayList<Repository>> repositories;
    private String endCursor;
    private boolean hasNextPage;

    public CreatedRepositoriesByMembers(HashMap<String, ArrayList<Repository>> repositories, String endCursor, boolean hasNextPage) {
        this.repositories = repositories;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public HashMap<String, ArrayList<Repository>> getRepositories() {
        return repositories;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

}

