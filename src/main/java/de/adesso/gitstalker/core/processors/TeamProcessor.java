package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.*;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.team_Resources.*;

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

    /**
     * SetUp method to set up the necessary data for processing. Already selects the matching organization.
     *
     * @param requestQuery           Contains the necessary information, for example the TeamRequest response.
     * @param requestRepository      RequestRepository for saving and checking outstanding requests.
     * @param organizationRepository OrganizationRepository for saving and extracting other stored information.
     */
    private void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.organization = this.organizationRepository.findByOrganizationName(requestQuery.getOrganizationName());
    }

    /**
     * Processing TeamRequest response.
     * Update of the RateLimit from the Github API. Check whether it is the last request of the type.
     *
     * @param requestQuery           Contains the necessary information, for example the TeamRequest response.
     * @param requestRepository      RequestRepository for saving and checking outstanding requests.
     * @param organizationRepository OrganizationRepository for saving and extracting other stored information.
     */
    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository);
        Data responseData = ((ResponseTeam) this.requestQuery.getQueryResponse()).getData();
        super.updateRateLimit(responseData.getRateLimit(), requestQuery.getQueryRequestType());
        this.processQueryResponse(responseData.getOrganization().getTeams());
        this.processRequestForRemainingInformation(responseData.getOrganization().getTeams().getPageInfo(), requestQuery.getOrganizationName());
        super.doFinishingQueryProcedure(requestRepository, organizationRepository, organization, requestQuery, RequestType.TEAM);
    }

    /**
     * Processing if additional requests are necessary for outstanding information. Otherwise the collected data will be added to the organization.
     *
     * @param pageInfo         Contains information about outstanding data.
     * @param organizationName Name related to the query.
     */
    private void processRequestForRemainingInformation(PageInfo pageInfo, String organizationName) {
        if (pageInfo.isHasNextPage()) {
            super.generateNextRequests(organizationName, pageInfo.getEndCursor(), RequestType.TEAM, requestRepository);
        } else {
            organization.addTeams(this.teams);
        }
    }

    /**
     * Processing the response from the TeamRequest. Creation of the team objects and saving in the HashMap.
     *
     * @param organizationTeams All teams of the organization.
     */
    private void processQueryResponse(Teams organizationTeams) {
        for (NodesTeams team : organizationTeams.getNodes()) {
            this.teams.put(team.getId(), new Team(team.getName(), team.getDescription(), team.getAvatarUrl(), team.getUrl(), this.processTeamMembers(team.getMembers().getNodes()), this.processTeamRepositories(team.getRepositories().getNodes())));

        }
    }

    /**
     * Processing of the members of the teams and selection of the suitable member object from the organization.
     *
     * @param members All members of the team.
     * @return ArrayList with the member object.
     */
    private ArrayList<Member> processTeamMembers(ArrayList<NodesMembers> members) {
        ArrayList<Member> teamMembers = new ArrayList<>();
        for (NodesMembers member : members) {
            if (this.organization.getMembers().containsKey(member.getId())) {
                teamMembers.add(this.organization.getMembers().get(member.getId()));
            }
        }
        return teamMembers;
    }

    /**
     * Processing of the repositories of the teams and selection of the suitable repository object from the organization.
     *
     * @param repositories All repositories of the team.
     * @return ArrayList with the repository object.
     */
    private ArrayList<Repository> processTeamRepositories(ArrayList<NodesRepositories> repositories) {
        ArrayList<Repository> teamRepositories = new ArrayList<>();
        for (NodesRepositories repository : repositories) {
            if (this.organization.getRepositories().containsKey(repository.getId())) {
                teamRepositories.add(this.organization.getRepositories().get(repository.getId()));
            }
        }
        return teamRepositories;
    }
}
