package objects.Team;

import java.util.HashMap;

public class Teams {

    private HashMap<String, Team> teams;
    private String endCursor;
    private boolean hasNextPage;

    public Teams(HashMap<String, Team> teams, String endCursor, boolean hasNextPage) {
        this.teams = teams;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public HashMap<String, Team> getTeams() {
        return teams;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
