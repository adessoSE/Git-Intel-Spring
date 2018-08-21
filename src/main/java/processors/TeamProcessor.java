package processors;

import enums.RequestType;
import objects.Member;
import objects.OrganizationWrapper;
import objects.Query;
import objects.Repository;
import objects.Team.Team;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import resources.team_Resources.*;

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
            this.teams.put(team.getId(), new Team(team.getName(), team.getDescription(), team.getAvatarUrl(), team.getUrl(), this.processTeamMembers(team.getMembers().getNodes()), this.processTeamRepositories(team.getRepositories().getNodes())));
        }
    }

    private HashMap<String, Member> processTeamMembers(ArrayList<NodesMembers> members) {
        HashMap<String, Member> teamMembers = new HashMap<>();
        for (NodesMembers member : members) {
            if (this.organization.getMembers().containsKey(member.getId())) {
                teamMembers.put(member.getId(), this.organization.getMembers().get(member.getId()));
            }
        }
        return teamMembers;
    }

    private HashMap<String, Repository> processTeamRepositories(ArrayList<NodesRepositories> repositories) {
        HashMap<String, Repository> teamRepositories = new HashMap<>();
        for (NodesRepositories repository : repositories) {
            if (this.organization.getRepositories().containsKey(repository.getId())) {
                teamRepositories.put(repository.getId(), this.organization.getRepositories().get(repository.getId()));
            }
        }
        return teamRepositories;
    }
}
