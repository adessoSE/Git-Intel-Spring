package de.adesso.processors;

import de.adesso.objects.Query;
import de.adesso.objects.ResponseWrapper;
import de.adesso.objects.Team.Team;
import de.adesso.objects.Team.TeamRepository;
import org.springframework.stereotype.Component;
import de.adesso.resources.team_Resources.NodesRepositories;
import de.adesso.resources.team_Resources.NodesTeams;
import de.adesso.resources.team_Resources.Teams;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class TeamProcessor extends ResponseProcessor {

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
        super.updateRateLimit(this.requestQuery.getQueryResponse().getResponseTeam().getData().getRateLimit(), requestQuery.getQueryRequestType());

        Teams organizationTeams = this.requestQuery.getQueryResponse().getResponseTeam().getData().getOrganization().getTeams();

        HashMap<String, Team> teams = new HashMap<>();
        for (NodesTeams team : organizationTeams.getNodes()) {
            teams.put(team.getId(), new Team(team.getName(), team.getDescription(), team.getAvatarUrl(), team.getUrl(), team.getMembers().getTotalCount(), processTeamRepositories(team)));
        }
        return new ResponseWrapper(new de.adesso.objects.Team.Teams(teams, organizationTeams.getPageInfo().getEndCursor(), organizationTeams.getPageInfo().isHasNextPage()));
    }

    private ArrayList<TeamRepository> processTeamRepositories(NodesTeams team) {
        ArrayList<TeamRepository> teamRepositories = new ArrayList<>();
        for (NodesRepositories repos : team.getRepositories().getNodes()) {
            teamRepositories.add(new TeamRepository(repos.getName(), repos.getDefaultBranchRef().getTarget().getHistory().getTotalCount()));
        }
        return teamRepositories;
    }
}
