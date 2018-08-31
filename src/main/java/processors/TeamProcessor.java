package processors;

import enums.RequestType;
import objects.OrganizationWrapper;
import objects.Query;
import objects.Team.Team;
import objects.Team.TeamRepository;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import resources.team_Resources.NodesRepositories;
import resources.team_Resources.NodesTeams;
import resources.team_Resources.PageInfo;
import resources.team_Resources.Teams;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

    private HashMap<String, Team> teams = new HashMap<>();

    public TeamProcessor() {
    }

    private void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.organization = this.organizationRepository.findByOrganizationName(requestQuery.getOrganizationName());
    }

    /**
     * Response processing of the Team request. Processing through every Team and save it in a ArrayList.
     * Creating a Teams object containing the Team ArrayList and the PageInfo wrapped into the ResponseWrapper.
     *
     * @return ResponseWrapper containing the Teams object.
     */
    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository);
        super.updateRateLimit(this.requestQuery.getQueryResponse().getResponseTeam().getData().getRateLimit(), requestQuery.getQueryRequestType());
        this.processQueryResponse(this.requestQuery.getQueryResponse().getResponseTeam().getData().getOrganization().getTeams());
        this.processRequestForRemainingInformation(this.requestQuery.getQueryResponse().getResponseTeam().getData().getOrganization().getTeams().getPageInfo(), requestQuery.getOrganizationName());
        super.doFinishingQueryProcedure(requestRepository, organizationRepository, organization, requestQuery, RequestType.TEAM);
    }

    private void processRequestForRemainingInformation(PageInfo pageInfo, String organizationName) {
        if (pageInfo.isHasNextPage()) {
            super.generateNextRequests(organizationName, pageInfo.getEndCursor(), RequestType.TEAM, requestRepository);
        } else {
            organization.addTeams(this.teams);
        }
    }

    private void processQueryResponse(Teams organizationTeams) {
        for (NodesTeams team : organizationTeams.getNodes()) {
            this.teams.put(team.getId(), new Team(team.getName(), team.getDescription(), team.getAvatarUrl(), team.getUrl(), team.getMembers().getTotalCount(), processTeamRepositories(team)));
        }
    }

    private ArrayList<TeamRepository> processTeamRepositories(NodesTeams team) {
        ArrayList<TeamRepository> teamRepositories = new ArrayList<>();
        for (NodesRepositories repos : team.getRepositories().getNodes()) {
            teamRepositories.add(new TeamRepository(repos.getName(), repos.getDefaultBranchRef().getTarget().getHistory().getTotalCount()));
        }
        return teamRepositories;
    }
}
