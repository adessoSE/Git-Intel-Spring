package objects.Team;

import java.util.ArrayList;

public class Teams {

    private ArrayList<Team> teams;
    private String endCursor;
    private boolean hasNextPage;

    public Teams(ArrayList<Team> teams, String endCursor, boolean hasNextPage) {
        this.teams = teams;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
