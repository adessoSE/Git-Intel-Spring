package processors;

import objects.Query;
import objects.ResponseWrapper;
import objects.Team.Team;
import objects.Team.TeamRepository;
import resources.team_Resources.NodesRepositories;
import resources.team_Resources.NodesTeams;
import resources.team_Resources.Teams;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamProcessor {

    private Query requestQuery;

    public TeamProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    /**
     * Response processing of the Team request. Processing through every Team and save it in a ArrayList.
     * Creating a Teams object containing the Team ArrayList and the PageInfo wrapped into the ResponseWrapper.
     *
     * @return ResponseWrapper containing the Teams object.
     */
    public ResponseWrapper processResponse() {
        Teams organizationTeams = this.requestQuery.getQueryResponse().getResponseTeam().getData().getOrganization().getTeams();

        HashMap<String, Team> teams = new HashMap<>();
        for (NodesTeams team : organizationTeams.getNodes()) {
            teams.put(team.getId(),new Team(team.getName(), team.getDescription(), team.getAvatarURL(),team.getUrl(), team.getMembers().getTotalCount(), processTeamRepositories(team)));
        }
        return new ResponseWrapper(new objects.Team.Teams(teams, organizationTeams.getPageInfo().getEndCursor(), organizationTeams.getPageInfo().isHasNextPage()));
    }

    private ArrayList<TeamRepository> processTeamRepositories(NodesTeams team) {
        ArrayList<TeamRepository> teamRepositories = new ArrayList<>();
        for (NodesRepositories repos : team.getRepositories().getNodes()) {
            teamRepositories.add(new TeamRepository(repos.getName(), repos.getDefaultBranchRef().getTarget().getHistory().getTotalCount()));
        }
        return teamRepositories;
    }
}
